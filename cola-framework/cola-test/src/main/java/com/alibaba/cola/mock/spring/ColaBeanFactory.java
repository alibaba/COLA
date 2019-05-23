package com.alibaba.cola.mock.spring;

import com.alibaba.cola.mock.proxy.MockDataProxy;
import com.alibaba.cola.mock.utils.MockHelper;

import org.mockito.Mockito;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author shawnzhan.zxy
 * @date 2019/04/28
 */
public class ColaBeanFactory extends DefaultListableBeanFactory{

    @Override
    protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
        throws BeanCreationException {
        try {
            return super.doCreateBean(beanName, mbd, args);
        }catch (BeanCreationException e){
            return newInstance(mbd, args);
        }
    }


    private Object newInstance(RootBeanDefinition mbd, Object... arg){
        Class clz = mbd.getTargetType();
        try {
            Class impClz = clz;
            Object oriTarget = Mockito.mock(impClz);
            return MockHelper.createMockFor(clz, new MockDataProxy(clz, oriTarget));
        }catch (Throwable e){
            return null;
        }
    }
}
