package com.alibaba.cola.mock;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.persist.ServiceListStore;
import com.alibaba.cola.mock.proxy.MockDataProxy;
import com.alibaba.cola.mock.scan.AnnotationTypeFilter;
import com.alibaba.cola.mock.scan.AssignableTypeFilter;
import com.alibaba.cola.mock.scan.AutoMockFactoryBean;
import com.alibaba.cola.mock.scan.FilterManager;
import com.alibaba.cola.mock.scan.RegexPatternTypeFilter;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.MockHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 集成测试回放控制器
 * @author shawnzhan.zxy
 * @date 2018/10/08
 */
public class ColaMockController implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor, InitializingBean
    ,ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ColaMockController.class);
    private final static String COLAMOCK_PROXY_FLAG = "\\$\\$EnhancerByColaMockWithCGLIB";
    protected static ConfigurableListableBeanFactory beanFactory;
    private static BeanDefinitionRegistry registry;
    /**
     * Config basePackage.
     */
    private List<String> serviceList;
    private FilterManager mockFilterManager;
    private FilterManager monitorFilterManager;
    private ServiceListStore serviceListStore = new ServiceListStore();

    public ColaMockController(String... basePackages){
        serviceList = serviceListStore.load();
        mockFilterManager = new FilterManager();
        List<ColaTestModel> colaTestModelList = ColaMockito.g().scanColaTest(basePackages);
        ColaMockito.g().getContext().setColaTestModelList(colaTestModelList);
        monitorFilterManager = new FilterManager();
        colaTestModelList.forEach(p->{
            monitorFilterManager.addAll(p.getTypeFilters());
        });

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ColaMockController.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ColaMockController.beanFactory = beanFactory;
        ColaMockito.g().setBeanFactory(beanFactory);
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Map<String, String> index = Arrays.stream(beanDefinitionNames).collect(Collectors.toMap(key->key, v->v));
        for(String name : serviceList){
            String[] meta = name.split(Constants.SERVICE_LIST_DELIMITER);
            String beanName = meta[0];
            String beanClass = meta[1];
            if(index.get(meta[0]) == null){
                try {
                    Class type = Thread.currentThread().getContextClassLoader().loadClass(beanClass);
                    //if("fileserverClient".equals(beanName)){
                    //    System.out.println("============");
                    //}
                    if(!mockFilterManager.match(type)){
                        continue;
                    }
                    GenericBeanDefinition definition = new GenericBeanDefinition();
                    definition.getConstructorArgumentValues().addGenericArgumentValue(beanName);
                    definition.getConstructorArgumentValues().addGenericArgumentValue(type);
                    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    definition.setBeanClass(AutoMockFactoryBean.class);
                    definition.setScope("singleton");
                    definition.setLazyInit(true);
                    definition.setAutowireCandidate(true);
                    registry.registerBeanDefinition(beanName, definition);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMockRegex(List<String> mockRegex) {
        mockRegex.forEach(p->{
            mockFilterManager.addFilter(new RegexPatternTypeFilter(p));
        });
    }

    public void setMockAssignable(List<String> mockAssignable) {
        mockAssignable.forEach(p->{
            try {
                Class clazz = Class.forName(p);
                mockFilterManager.addFilter(new AssignableTypeFilter(clazz));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void setMockAnnotation(List<String> mockAnnotation) {
        mockAnnotation.forEach(p->{
            try {
                Class<? extends Annotation> annotationCls = (Class<? extends Annotation>)Class.forName(p);
                mockFilterManager.addFilter(new AnnotationTypeFilter(annotationCls));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().getName().indexOf(Constants.COLAMOCK_PROXY_FLAG) > -1){
            ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(bean.getClass(), beanName, null, bean));
            return bean;
        }
        if(monitorFilterManager.match(bean.getClass())){
            MockDataProxy mockDataProxy = new MockDataProxy(bean.getClass(), bean);
            try{
                Object colaProxy = MockHelper.createMockFor(bean.getClass(), mockDataProxy);
                ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(bean.getClass(), beanName, bean, colaProxy));
                bean = colaProxy;
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(mockFilterManager.getFilterList().size() == 0){
            mockFilterManager.addFilter(new RegexPatternTypeFilter(".*"));
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        printMockObjectList();
    }

    private void printMockObjectList(){
        logger.info("===mock object list===");
        int cnt = 0;
        for(MockServiceModel m : ColaMockito.g().getContext().getMonitorList()){
            if(m.getInterfaceCls().getName().indexOf(Constants.COLAMOCK_PROXY_FLAG) < 0){
                continue;
            }
            cnt++;
            String[] sp = m.getInterfaceCls().getName().split(COLAMOCK_PROXY_FLAG);
            logger.info("serviceName:" + m.getServiceName() + ",class:" + sp[0]);
        }
        logger.info("===mock object list===" + cnt);
    }
}
