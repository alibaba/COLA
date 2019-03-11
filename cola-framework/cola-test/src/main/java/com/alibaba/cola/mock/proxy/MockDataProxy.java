package com.alibaba.cola.mock.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.InputParamsOfOneMethod;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.utils.CommonUtils;
import com.alibaba.cola.mock.utils.CompareUtils;
import com.alibaba.cola.mock.utils.DeepCopy;
import com.alibaba.cola.mock.utils.MockHelper;
import com.alibaba.cola.mock.utils.reflection.BeanPropertySetter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

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
        return invokeMethod(o, method, objects, methodProxy);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return invokeMethod(o, method, objects, null);
    }

    public Object invokeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        boolean isMock = false;
        //检查本次要mock的对象
        MockServiceModel mockServiceModel = ColaMockito.g().getContext().getMockByIntf(mapperInterface);
        if(MockHelper.isMonitorMethod(method.getName())
            && mockServiceModel != null){
            isMock = true;
        }
        if(isMock){
            String mockDataId = mapperInterface.getName()+"_"+method.getName();
            MockDataFile mockDataFile = ColaMockito.g().getFileDataEngine().getMockDataFileByFileId(ColaMockito.g().getCurrentTestUid());
            if(dataHasBeenMocked(mockDataId, mockDataFile)){
                //自动对比mock接口的入参
                if(needCompareThisMethodInputs(method)){
                    autoCheckParams(mockDataId, objects);
                }
                //返回mock的返回值数据
                result = mockDataFile.getData(mockDataId, method.getReturnType());
                result = DeepCopy.from(result);
                throwIfException(result);
            }
            return result;
        }
        method.setAccessible(true);
        if(instance == null){
            //System.out.println(String.format("leaf mock[%s],no instance",o.getClass().getSuperclass()));
            return null;
        }
        result = method.invoke(instance, objects);
        return result;
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

    private void autoCheckParams(String mockdDataId, Object[] objects ) {
        InputParamsFile inputParamsFile = ColaMockito.g().getFileDataEngine().getInputParamsFileByFileId(
            ColaMockito.g().getCurrentTestUid()+ "_inputParams");

        if (inputParamsFile.contain(mockdDataId)) {
            InputParamsOfOneMethod inputParamsOfOneMethod = inputParamsFile.getCurrentInputOfOneMethod(mockdDataId);
            Object[] recordInputParams = inputParamsOfOneMethod.getInutParams();

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
                    throw new RuntimeException(compareResult);
                }
            }

        }

    }

    private ColaTestModel getCurrentTestClassColaTestModel(){

        ColaTestModel currentTestClassConfigModel = ColaMockito.g().getContext().getColaTestModelMap().get(
            ColaMockito.g().getContext().getTestMeta().getTestClass());
        return currentTestClassConfigModel;
    }



    private Class[] getNoNeedComparedInterface(){
        ColaTestModel currentTestModel = getCurrentTestClassColaTestModel();

        if(currentTestModel == null){
            return null;
        }

        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(ColaMockito.g().getCurrentTestUid());
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
        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(ColaMockito.g().getCurrentTestUid());
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

        ExcludeCompare config = currentTestModel.getCurrentTestMethodConfig(ColaMockito.g().getCurrentTestUid());
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



}
