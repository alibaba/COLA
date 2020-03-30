package com.alibaba.cola.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.model.ServiceModel;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.persist.DataStoreEnum;
import com.alibaba.cola.mock.persist.FileDataEngine;
import com.alibaba.cola.mock.persist.ServiceListStore;
import com.alibaba.cola.mock.persist.ColaTestModelStore;
import com.alibaba.cola.mock.proxy.DataRecordProxy;
import com.alibaba.cola.mock.proxy.FactoryBeanProxy;
import com.alibaba.cola.mock.proxy.OnlineDataRecordProxy;
import com.alibaba.cola.mock.scan.FilterManager;
import com.alibaba.cola.mock.scan.RegexPatternTypeFilter;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.MockHelper;

import org.junit.runner.Description;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 数据录制控制器
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class ColaRecordController extends AbstractRecordController{
    private String recordConfigFile;
    private String saveDir;
    private ColaTestModelStore colaTestModelStore;
    private FilterManager baseFilterManager = new FilterManager();
    private FilterManager excludeFilterManager = new FilterManager();
    /** 录制入口记录*/
    public static ThreadLocal<ColaMockito> mainRecord = new ThreadLocal<>();
    /** 录制开关，启动时 开关打开会生成插入观察点，否则不生成观察点*/
    private boolean recordMode = false;
    /**
     * Config basePackage.
     */
    private String[] basePackages;
    private String[] excludeRegex;


    @Override
    public void init(){
        ColaMockito.g().setFileDataEngine(new FileDataEngine(DataStoreEnum.JSON_STORE, saveDir));
        colaTestModelStore = new ColaTestModelStore(recordConfigFile);
        serviceListStore = new ServiceListStore(saveDir);
    }

    public void runRecord(String testName){
        if(!recordMode){
            throw new RuntimeException("未开启录制模式");
        }
        String[] params = testName.split("#");
        if(params.length != 2){
            throw new RuntimeException("参数不合法!格式 class#methodName");
        }
        String clazz = params[0];
        String methodName = params[1];

        ColaMockito.g().getContext().setRecording(true);
        Map<String, ColaTestModel> testModelMap = reloadConfigFile();
        ColaTestModel colaTestModel = testModelMap.get(clazz);
        List<ColaTestModel> colaTestModelList = new ArrayList<>();
        if(colaTestModel == null){
            throw new RuntimeException(String.format("testName[%s] cannot find!", testName));
        }
        colaTestModelList.add(colaTestModel);
        ColaMockito.g().getContext().setColaTestModelList(colaTestModelList);

        Description description = Description.createTestDescription(colaTestModel.getTestClazz(), methodName);
        ColaMockito.g().getContext().setColaTestMeta(new ColaTestDescription(description));
    }

    public void stopRecord(){
        ColaMockito.g().getContext().setColaTestMeta(null);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(!recordMode){
            return bean;
        }
        if(beanName.indexOf(Constants.INNER_BEAN_NAME) > -1) {
            return bean;
        }

        BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
        String metaClassName = getClassName(bean, beanDefinition);
        try {
            Class factCls = Class.forName(metaClassName);
            serviceSet.add(new ServiceModel(beanName, factCls));
            Object oriBean = bean;
            try {
                if(!baseFilterManager.match(factCls)
                    || excludeFilterManager.match(factCls)){
                    return bean;
                }
                if(bean.getClass().getName().indexOf(Constants.COLAMOCK_PROXY_FLAG) > 0){
                    return bean;
                }
                if (bean instanceof FactoryBean) {
                    FactoryBeanProxy mapperProxy = new FactoryBeanProxy(factCls, bean, beanName, true);
                    bean = MockHelper.createMockFor(bean.getClass(), mapperProxy);
                } else {
                    DataRecordProxy mapperProxy = new OnlineDataRecordProxy(factCls, bean);
                    bean = MockHelper.createMockFor(factCls, mapperProxy);
                    //MainRecordProxy mainRecordProxy = new MainRecordProxy(factCls, bean);
                    //bean = ColaMockito.g().createMockFor(factCls, mainRecordProxy);

                    ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(factCls, beanName, oriBean, bean));
                }
            }catch (Exception e){
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void setRecordConfigFile(String recordConfigFile) {
        this.recordConfigFile = recordConfigFile;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public void setRecordMode(boolean recordMode) {
        this.recordMode = recordMode;
    }

    public void setBasePackages(String... basePackages) {
        this.basePackages = basePackages;
        if(basePackages.length > 0){
            for(String basePackage : basePackages){
                baseFilterManager.addFilter(new RegexPatternTypeFilter(basePackage));
            }
        }
    }

    public void setExcludeRegex(String... excludeRegex) {
        this.excludeRegex = excludeRegex;
        if(excludeRegex.length > 0){
            for(String regex : excludeRegex){
                excludeFilterManager.addFilter(new RegexPatternTypeFilter(regex));
            }
        }
    }

    private Map<String, ColaTestModel> reloadConfigFile(){
        List<ColaTestModel> colaTestModelList = colaTestModelStore.load();
        if(colaTestModelList == null){
            return new HashMap<>();
        }

        return colaTestModelList.stream().collect(Collectors.toMap(p->p.getTestClazz().getName(), p->p));
    }

}
