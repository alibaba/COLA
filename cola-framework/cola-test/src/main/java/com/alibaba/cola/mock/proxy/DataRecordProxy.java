package com.alibaba.cola.mock.proxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.cola.container.command.AbstractCommand;
import com.alibaba.cola.container.command.TestMethodRunCmd;
import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.InputParamsOfOneMethod;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.DeepCopy;
import com.alibaba.cola.mock.utils.MockHelper;
import com.alibaba.cola.mock.utils.StackSearcher;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/02
 */
public class DataRecordProxy implements MethodInterceptor,InvocationHandler,ColaProxyI {
    protected Class<?> mapperInterface;
    private Object instance;
    private MockDataProxy mockDataProxy;

    public DataRecordProxy(Class mapperInterface, Object instance){
        this.mapperInterface = mapperInterface;
        this.instance = instance;
        this.mockDataProxy = new MockDataProxy(mapperInterface, instance);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        try{
            return invokeMethod(o, method, objects, methodProxy);
        }catch (InvocationTargetException e){
            throw e.getTargetException();
        }
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        try{
            return invokeMethod(o, method, objects, null);
        }catch (InvocationTargetException e){
            throw e.getTargetException();
        }
    }

    public Object invokeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //局部录制会走到此逻辑
        if(isMock(method)){
            return mockDataProxy.invokeMethod(o, method, objects, methodProxy);
        }

        if(!isNeedRecord(method)){
            return executeMethod(method, objects);
        }

        beforeMethod(o, method, objects, methodProxy);
        Object result = executeMethod(method, objects);
        result = afterMethod(method, objects, result);
        return result;
    }

   protected void beforeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy){
        try {
            String mockDataId = getMockDataStorageKey(method);
            recordInputData(objects, mockDataId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    protected Object afterMethod(Method method, Object[] params, Object result){
        if(!MockHelper.isMonitorMethod(method.getName())){
            return result;
        }
        storeMockOutputData(method, result, params);
        return result;
    }

    /**
     * 暂存方法返回值，用作回放的mock数据
     * @param method
     * @param result
     */
    protected void storeMockOutputData(Method method, Object result, Object[] inputs) {
        try {
            String mockDataId = getMockDataStorageKey(method);
            ColaMockito.g().getContext().getStackTree().recordCurrentStackPoint(getColaMockito().getCurrentTestModel());
            recordOutputData(result, mockDataId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void recordOutputData(Object result, String mockDataId) throws IOException, ClassNotFoundException {
        MockDataFile mockDataFile = getColaMockito().getFileDataEngine().createAndGetMockDataFileByFileId(getMockFileName());
        mockDataFile.putData(mockDataId, DeepCopy.from(result));
    }

    /**
     * 获取录制文件名
     * @return
     */
    protected String getMockFileName(){
        return getColaMockito().getCurrentTestUid();
    }

    protected ColaMockito getColaMockito(){
        return ColaMockito.g();
    }

    private void recordInputData(Object[] inputs, String mockDataId) throws IOException, ClassNotFoundException {
        InputParamsFile inputParamsFile = getColaMockito().getFileDataEngine().createAndGetInputParamsFileByFileId(
            getMockFileName() + Constants.INPUT_PARAMS_FILE_SUFFIX);

        InputParamsOfOneMethod inputParams = new InputParamsOfOneMethod();
        inputParams.setInutParams(DeepCopy.from(inputs));
        inputParamsFile.putData(mockDataId, inputParams);
    }

    //manufacture功能，暂时不用了
    //private Object executeMethod(Method method, Object[] objects) throws Exception {
    //    Object result;
    //    ColaTestModel colaTestModel = getColaMockito().getCurrentTestModel();
    //    if (canRecord() && colaTestModel.matchDataManufactureFilter(mapperInterface)) {
    //        result = manufactureDataIfNotExists(colaTestModel, method);
    //        return result;
    //    }
    //    return executeOriMethod(method, objects);
    //}

    private Object executeMethod(Method method, Object[] objects) throws Exception{
        Object result;
        method.setAccessible(true);
        //result = method.invoke(o, objects);匪夷所思之处，method在某些场景(ABService)下会报错，object not instance
        Method oriMethod = null;
        try {
            oriMethod = instance.getClass().getMethod(method.getName(), method.getParameterTypes());
        }catch (NoSuchMethodException e){
            oriMethod = instance.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }
        oriMethod.setAccessible(true);
        result = oriMethod.invoke(instance, objects);
        return result;
    }

    private Object manufactureDataIfNotExists(ColaTestModel colaTestModel, Method method){
        String mockDataId = getMockDataStorageKey(method);
        Object result = colaTestModel.getDataMap(mockDataId);
        if(result != null){
            return result;
        }
        Class<?> returnType = method.getReturnType();
        if(returnType != null && !Constants.RETURN_TYPE_VOID.equals(returnType.toString())){
            result = ColaMockito.pojo().manufacturePojo(returnType);
            colaTestModel.saveDataMap(mockDataId, result);
        }
        return result;
    }

    private String getMockDataStorageKey(Method method) {
        return mapperInterface.getName() + "_" + method.getName();
    }

    protected boolean recordStarted(){
        if(getColaMockito() == null){
            return false;
        }
        if(getColaMockito().getCurrentTestModel() == null){
            return false;
        }

        return getColaMockito().getContext().isRecording();
    }

    private boolean isNeedRecord(Method method){
        ColaTestModel colaTestModel = getColaMockito().getCurrentTestModel();
        if (!(recordStarted() && colaTestModel.matchMockFilter(mapperInterface))) {
            return false;
        }
        if(StackSearcher.isTopMockPoint(colaTestModel, mapperInterface, method.getName())){
            return true;
        }
        return false;
    }

    /**
     * 判断是否Mock接口
     * @return
     */
    private boolean isMock(Method method){
        if(!recordStarted()){
            return false;
        }
        AbstractCommand command = ColaMockito.g().getContext().getColaTestMeta().getCommand();
        if(!(command instanceof TestMethodRunCmd)){
            return false;
        }
        TestMethodRunCmd testMethodRunCmd = (TestMethodRunCmd)command;
        if(testMethodRunCmd.matchRecordFilter(mapperInterface + Constants.NOTE_SYMBOL + method.getName())){
            return false;
        }
        return true;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
}
