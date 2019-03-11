package com.alibaba.cola.mock;

import java.util.List;

import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.model.ServiceModel;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.proxy.DataRecordProxy;
import com.alibaba.cola.mock.scan.FilterManager;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.MockHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 数据录制控制器
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class ColaTestRecordController extends AbstractRecordController {
    /**
     * Config basePackage.
     */
    private String[] basePackages;
    private static Class templateSuperClass;
    private FilterManager monitorFilterManager = new FilterManager();

    public static String getTemplateSuperClassName(){
        if(templateSuperClass == null){
            return StringUtils.EMPTY;
        }
        return templateSuperClass.getName();
    }

    public ColaTestRecordController(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void init(){
        try {
            ColaMockito.g().getContext().setRecording(true);
            List<ColaTestModel> colaTestModelList = ColaMockito.g().scanColaTest(basePackages);
            ColaMockito.g().getContext().setColaTestModelList(colaTestModelList);
            colaTestModelList.forEach(p -> {
                monitorFilterManager.addAll(p.getTypeFilters());
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.indexOf(Constants.INNER_BEAN_NAME) > -1) {
            return bean;
        }
        BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
        String metaClassName = getClassName(bean, beanDefinition);
        try {
            Class factCls = Class.forName(metaClassName);
            serviceSet.add(new ServiceModel(beanName, factCls));
            if(!(monitorFilterManager.match(factCls) && bean.getClass().getName().indexOf(Constants.COLAMOCK_PROXY_FLAG)<0)){
                return bean;
            }

            if (bean instanceof FactoryBean) {
                return bean;
            }

            DataRecordProxy mapperProxy = new DataRecordProxy(factCls, bean);
            Object oriBean = bean;
            //不能给代理再生成代理，改成了装饰器模式
            bean = MockHelper.createMockFor(factCls, mapperProxy);
            ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(factCls, beanName, oriBean, bean));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void setTemplateSuperClass(Class templateSuperClass) {
        ColaTestRecordController.templateSuperClass = templateSuperClass;
    }
}
