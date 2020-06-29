package com.alibaba.cola.mock;

import java.util.List;
import java.util.Map;

import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.persist.DataMapStore;
import com.alibaba.cola.mock.persist.DataStoreEnum;
import com.alibaba.cola.mock.persist.FileDataEngine;
import com.alibaba.cola.mock.runner.AbstractColaTest;
import com.alibaba.cola.mock.runner.IntegrateColaTest;
import com.alibaba.cola.mock.runner.UnitColaTest;
import com.alibaba.cola.mock.scan.ClassPathTestScanner;
import com.alibaba.cola.mock.utils.DeepCopy;
import com.alibaba.cola.mock.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/02
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
        T data = ColaMockito.g().getCurrentTestModel().getDataMap4Strict(key);
        if(data != null){
            try {
                return DeepCopy.from(data);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return data;
    }

    public static PodamFactory pojo(){
        return podamFactory;
    }

    /**
     * 测试类mock回放初始化
     */
    public void initMock(ColaTestDescription desc){
        AbstractColaTest colaTest = new IntegrateColaTest(this);
        colaTest.init(desc);
    }

    /**
     * 测试类mock回放初始化
     */
    public void initUnitMock(ColaTestDescription desc){
        AbstractColaTest colaTest = new UnitColaTest(this);
        colaTest.init(desc);
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
        return FileUtils.getAbbrOfClassName(context.getDescription().getClassName()) + "_"
            + context.getDescription().getMethodName();
        //return context.getDescription().getClassName() + "_"
        //    + context.getDescription().getMethodName();
    }





    public ColaTestModel getCurrentTestModel(){
        if(context.getColaTestMeta() == null){
            return null;
        }
        Map<Class, ColaTestModel> colaTestModelMap = context.getColaTestModelMap();
        if(colaTestModelMap == null){
            throw new RuntimeException("not ready ColaTestModel,please check unit test or integrate test!");
        }
        return colaTestModelMap.get(context.getDescription().getTestClass());
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

    public DataMapStore getDataMapStore() {
        return dataMapStore;
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
