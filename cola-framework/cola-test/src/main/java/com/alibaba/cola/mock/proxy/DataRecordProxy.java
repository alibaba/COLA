package com.alibaba.cola.mock.proxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.InputParamsOfOneMethod;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.DeepCopy;
import com.alibaba.cola.mock.utils.MockHelper;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class DataRecordProxy implements MethodInterceptor,InvocationHandler {
    protected Class<?> mapperInterface;
    private Object instance;

    public DataRecordProxy(Class mapperInterface, Object instance){
        this.mapperInterface = mapperInterface;
        this.instance = instance;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return invokeMethod(o, method, objects, methodProxy);
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return invokeMethod(o, method, objects, null);
    }

    public Object invokeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        beforeMethod(o, method, objects, methodProxy);
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
        result = afterMethod(method, objects, result);
        return result;
    }

    protected void beforeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy){
        try {
            ColaTestModel colaTestModel = getColaMockito().getCurrentTestModel();
            if(colaTestModel == null){
                return;
            }
            if (colaTestModel.filterInclude(mapperInterface)) {
                String mockDataId = getMockDataStorageKey(method);
                recordInputData(objects, mockDataId);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    protected Object afterMethod(Method method, Object[] params, Object result){
        if(!MockHelper.isMonitorMethod(method.getName())){
            return result;
        }
        if(getColaMockito() == null || !getColaMockito().isRecording()){
            return result;
        }
        storeMockInputAndOutputData(method, result, params);
        return result;
    }

    protected ColaMockito getColaMockito(){
        return ColaMockito.g();
    }



    /**
     * 暂存方法返回值，用作回放的mock数据
     * @param method
     * @param result
     */
    protected void storeMockInputAndOutputData(Method method, Object result, Object[] inputs) {
        try {
            String mockDataId = getMockDataStorageKey(method);
            ColaTestModel colaTestModel = getColaMockito().getCurrentTestModel();
            if (colaTestModel.filterInclude(mapperInterface)) {
                recordOutputData(result, mockDataId);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取录制文件名
     * @return
     */
    protected String getMockFileName(){
        return getColaMockito().getCurrentTestUid();
    }

    private void recordInputData(Object[] inputs, String mockDataId) throws IOException, ClassNotFoundException {
        InputParamsFile inputParamsFile = getColaMockito().getFileDataEngine().createAndGetInputParamsFileByFileId(
            getMockFileName() + Constants.INPUT_PARAMS_FILE_SUFFIX);

        InputParamsOfOneMethod inputParams = new InputParamsOfOneMethod();
        inputParams.setInutParams(DeepCopy.from(inputs));
        inputParamsFile.putData(mockDataId, inputParams);
    }

    private void recordOutputData(Object result, String mockDataId) throws IOException, ClassNotFoundException {
        MockDataFile mockDataFile = getColaMockito().getFileDataEngine().createAndGetMockDataFileByFileId(getMockFileName());
        mockDataFile.putData(mockDataId, DeepCopy.from(result));
    }

    private String getMockDataStorageKey(Method method) {
        return mapperInterface.getName() + "_" + method.getName();
    }

}
