package com.alibaba.cola.mock.scan;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.proxy.MockDataProxy;
import com.alibaba.cola.mock.utils.MockHelper;
import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

/**
 * mapper factory bean
 *
 * @author shawnzhan.zxy
 * @date 2018/09/24
 */
public class AutoMockFactoryBean<T> implements FactoryBean<T> {

    private String beanName;
    private Class<T> mapperInterface;

    public AutoMockFactoryBean(String beanName, Class<T> mapperInterface) {
        this.beanName = beanName;
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        Object bean = getProxy(this.mapperInterface);
        return (T)bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }


    /**
     * Return the mapper interface of the MyBatis mapper
     *
     * @return class of the interface
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Object getProxy(Class<T> clazz){
        Object colaProxy = null;
        Object oriTarget = Mockito.mock(mapperInterface);
        MockDataProxy mockDataProxy = new MockDataProxy(mapperInterface, oriTarget);
        try{
            colaProxy = MockHelper.createMockFor(mapperInterface, mockDataProxy);
        }catch(Exception e){
            e.printStackTrace();
        }
        ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(mapperInterface, beanName, oriTarget, colaProxy));
        return colaProxy;
    }
}
