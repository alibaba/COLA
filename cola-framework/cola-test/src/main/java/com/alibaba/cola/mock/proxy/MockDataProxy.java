package com.alibaba.cola.mock.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.model.InputListOfSameNameMethod;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.InputParamsOfOneMethod;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.utils.CommonUtils;
import com.alibaba.cola.mock.utils.CompareUtils;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.DeepCopy;
import com.alibaba.cola.mock.utils.MockHelper;
import com.alibaba.cola.mock.utils.reflection.BeanPropertySetter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

import static com.alibaba.cola.mock.ColaMockito.g;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class MockDataProxy implements MethodInterceptor,InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(MockDataProxy.class);
    private Class<?> mapperInterface;
    private Object instance;

    public MockDataProxy(Class mapperInterface, Object instance){
        this.mapperInterface = mapperInterface;
        this.instance = instance;
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
        //检查本次要mock的对象
        if(isMockService(method)){
            return exceuteMock(method, objects);
        }
        if (isDataManufacture()) {
            return manufactureDataIfNotExists(method);
        }

        method.setAccessible(true);
        if(instance == null){
            return null;
        }
        return method.invoke(instance, objects);
    }

    private Object exceuteMock(Method method, Object[] objects) throws Exception{
        Object result = null;
        String mockDataId = getMockDataStorageKey(method);
        MockDataFile mockDataFile = g().getFileDataEngine().getMockDataFileByFileId(g().getCurrentTestUid());
        if(!dataHasBeenMocked(mockDataId, mockDataFile)) {
            return result;
        }
        //自动对比mock接口的入参
        autoCheckParams(method, mockDataId, objects);
        //返回mock的返回值数据
        result = mockDataFile.getData(mockDataId, method.getReturnType());
        ColaMockito.g().getContext().getStackTree().recordCurrentStackPoint(ColaMockito.g().getCurrentTestModel());
        result = DeepCopy.from(result);
        throwIfException(result);
        return convertResultIfEnum(method.getReturnType(), result);
    }

    private boolean isMockService(Method method){
        return MockHelper.isMonitorMethod(method.getName())
            && getCurrentTestClassColaTestModel() != null && getCurrentTestClassColaTestModel().matchMockFilter(mapperInterface);
    }

    private boolean isDataManufacture(){
        ColaTestModel colaTestModel = getCurrentTestClassColaTestModel();
        return colaTestModel != null && colaTestModel.matchDataManufactureFilter(mapperInterface);
    }

    private boolean needCompareThisMethodInputs( Method currentMockedMethod){

        if (isInExcludeInterfaceList()) { return false; }

        if (isInExcludeMethodList(currentMockedMethod)) { return false; }

        return true;
    }

    private boolean isInExcludeInterfaceList() {
        Class[] excludeInterfaces = getNoNeedComparedInterface();
        if(excludeInterfaces != null && excludeInterfaces.length > 0) {
            for(Class exclude : excludeInterfaces) {
                if (mapperInterface.getSimpleName().equals(exclude.getSimpleName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInExcludeMethodList(Method currentMockedMethod) {

        String[] excludeMethods = getNoNeedComparedMethod();

        if(excludeMethods != null && excludeMethods.length >0) {
            for(String exclude : excludeMethods) {
                if (currentMockedMethod.getName().equals(exclude)) {
                    return true;
                }
            }
        }

        return false;
    }



    private boolean dataHasBeenMocked(String mockDataId, MockDataFile mockDataFile) {
        return mockDataFile.contain(mockDataId);
    }

    private void autoCheckParams(Method method, String mockdDataId, Object[] objects ) {
        InputParamsFile inputParamsFile = g().getFileDataEngine().getInputParamsFileByFileId(
            g().getCurrentTestUid()+ "_inputParams");

        if (inputParamsFile.contain(mockdDataId)) {
            InputListOfSameNameMethod inputMethod = inputParamsFile.getInputListOfSameNameMethod(mockdDataId);
            InputParamsOfOneMethod inputParamsOfOneMethod = inputParamsFile.getCurrentInputOfOneMethod(mockdDataId);
            Object[] recordInputParams = inputParamsOfOneMethod.getInutParams();
            boolean isNeedCompare = needCompareThisMethodInputs(method);
            if(!isNeedCompare){
                return;
            }
            if(objects == null && recordInputParams != null){
                throw new RuntimeException(
                    "mock interface input params compare wrong,record inputParams is not null while current inputs is null,class_method is:" + mockdDataId);
            }

            if(objects != null && recordInputParams == null){
                throw new RuntimeException(
                    "mock interface input params compare wrong,record inputParams is  null while current inputs is not null,class_method is:" + mockdDataId);
            }
            if (objects != null && recordInputParams != null && objects.length != recordInputParams.length) {
                throw new RuntimeException(
                    "mock interface input params compare wrong,size is unmatch, class_method is:" + mockdDataId);
            }

            for(int i=0; i< objects.length ;i++){
                Object currentInput = objects[i];
                Object recordInput = recordInputParams[i];

                String compareResult  = CompareUtils.compareFields(currentInput, recordInput, getNoNeedComparedFields());

                if(!StringUtils.isEmpty(compareResult)){
                    throw new RuntimeException(String.format(Constants.DATA_CURSOR_DESC, inputMethod.getCurIndex(), i)
                        + compareResult);
                }
            }

        }

    }

    private ColaTestModel getCurrentTestClassColaTestModel(){
        if(ColaMockito.g().getContext().getColaTestMeta() == null){
            return null;
        }

        Class testClass = ColaMockito.g().getContext().getDescription().getTestClass();
        ColaTestModel currentTestClassConfigModel = ColaMockito.g().getContext()
            .getColaTestModelMap().get(testClass);
        return currentTestClassConfigModel;
    }

    private Class[] getNoNeedComparedInterface(){
        ColaTestModel currentTestModel = getCurrentTestClassColaTestModel();

        if(currentTestModel == null){
            return null;
        }

        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(g().getCurrentTestUid());
        if(config != null){
            return config.mockedInterfaces();
        }else{
            return null;
        }
    }


    private String[] getNoNeedComparedMethod(){

        ColaTestModel currentTestModel = getCurrentTestClassColaTestModel();

        if(currentTestModel == null){
            return null;
        }
        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(g().getCurrentTestUid());
        if(config != null){
            return config.mockedMethods();
        }else{
            return null;
        }
    }

    private String[] getNoNeedComparedFields(){

        ColaTestModel currentTestModel = getCurrentTestClassColaTestModel();

        if(currentTestModel == null){
            return null;
        }

        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(g().getCurrentTestUid());
        if(config != null){
            return config.fields();
        }else{
            return null;
        }
    }

    private boolean throwIfException(Object data) throws Exception {
        if(data == null || !(data instanceof Map)){
            return false;
        }
        Map dataMap = (Map)data;
        String exceptionClazz = CommonUtils.convert(dataMap.get("@throw"), String.class);
        if(org.apache.commons.lang3.StringUtils.isBlank(exceptionClazz)){
            return false;
        }
        Object exceptionInstance = MockHelper.newInstance(exceptionClazz);
        dataMap.put("cause", new RuntimeException());
        Iterator<Entry> it = dataMap.entrySet().iterator();
        while (it.hasNext()){
            Entry entry = it.next();
            String fieldName = CommonUtils.convert(entry.getKey(), String.class);
            BeanPropertySetter propertySetter = new BeanPropertySetter(exceptionInstance, fieldName);
            propertySetter.setValue(entry.getValue());
        }
        throw (Exception)exceptionInstance;
    }

    private Object manufactureDataIfNotExists(Method method){
        ColaTestModel colaTestModel = getCurrentTestClassColaTestModel();
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

    private Object convertResultIfEnum(Class clazz, Object value){
        if(clazz.isEnum() && value != null){
            return Enum.valueOf(clazz, value.toString());
        }
        return value;
    }

    private String getMockDataStorageKey(Method method) {
        return mapperInterface.getName() + "_" + method.getName();
    }
}
