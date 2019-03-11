package com.alibaba.cola.mock;

import java.util.List;
import java.util.Map;

import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.persist.DataMapStore;
import com.alibaba.cola.mock.persist.DataStoreEnum;
import com.alibaba.cola.mock.persist.FileDataEngine;
import com.alibaba.cola.mock.runner.AbstractColaTest;
import com.alibaba.cola.mock.runner.IntegrateColaTest;
import com.alibaba.cola.mock.runner.UnitColaTest;
import com.alibaba.cola.mock.scan.ClassPathTestScanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class ColaMockito {
    private static final Logger logger = LoggerFactory.getLogger(ColaMockito.class);
    public static ColaMockito instance = new ColaMockito();
    static PodamFactory podamFactory = new PodamFactoryImpl();
    private ColaMockContext context = new ColaMockContext();
    private FileDataEngine fileDataEngine = new FileDataEngine(DataStoreEnum.JSON_STORE);
    private DataMapStore dataMapStore = new DataMapStore();
    private ListableBeanFactory beanFactory;

    public static ColaMockito g(){
        return instance;
    }

    public static<T> T mock(T t){
        return (T)ColaMockito.g().context.getMockByMInstance(t);
    }

    public static<T> T getDataMap(String key){
        return ColaMockito.g().getCurrentTestModel().getDataMap(key);
    }

    public static PodamFactory pojo(){
        return podamFactory;
    }

    /**
     * 测试类mock回放初始化
     * @param testInstance
     */
    public void initMock(Object testInstance){
        AbstractColaTest colaTest = new IntegrateColaTest(this);
        colaTest.init(testInstance);
    }

    /**
     * 测试类mock回放初始化
     * @param testInstance
     */
    public void initUnitMock(Object testInstance){
        UnitColaTest colaTest = new UnitColaTest(this);
        colaTest.init(testInstance);
    }

    /**
     * 扫描指定包下的test类
     * @param basePackages
     * @return
     */
    public List<ColaTestModel> scanColaTest(String... basePackages){
        List<ColaTestModel> colaTestModelList = null;
        try {
            ClassPathTestScanner testScanner = new ClassPathTestScanner();
            colaTestModelList = testScanner.scanColaTests(basePackages);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return colaTestModelList;
    }

    /**
     * 扫描指定test类
     * @param testClazz
     * @return
     */
    public ColaTestModel scanColaTest(Class testClazz){
        ClassPathTestScanner testScanner = new ClassPathTestScanner();
        return testScanner.scanColaTest(testClazz);
    }

    public ColaMockContext getContext() {
        return context;
    }

    public String getCurrentTestUid() {
        return context.getTestMeta().getClassName() + "_" + context.getTestMeta().getMethodName();
    }

    public ColaTestModel getCurrentTestModel(){
        if(context.getTestMeta() == null){
            return null;
        }
        Map<Class, ColaTestModel> colaTestModelMap = context.getColaTestModelMap();
        if(colaTestModelMap == null){
            throw new RuntimeException("not ready ColaTestModel,please check unit test or integrate test!");
        }
        return colaTestModelMap.get(context.getTestMeta().getTestClass());
    }

    public boolean isRecording(){
        if(getCurrentTestModel() == null){
            return false;
        }
        return true;
    }

    public void setContext(ColaMockContext context) {
        this.context = context;
    }

    public FileDataEngine getFileDataEngine() {
        return fileDataEngine;
    }

    public Map<String, Object> loadDataMaps(String classPath){
        return dataMapStore.load(classPath);
    }

    public void setFileDataEngine(FileDataEngine fileDataEngine) {
        this.fileDataEngine = fileDataEngine;
    }

    public ListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
