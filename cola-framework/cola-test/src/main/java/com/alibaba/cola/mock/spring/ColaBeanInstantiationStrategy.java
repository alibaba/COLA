package com.alibaba.cola.mock.spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.alibaba.cola.mock.proxy.MockDataProxy;
import com.alibaba.cola.mock.utils.MockHelper;

import org.mockito.Mockito;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author shawnzhan.zxy
 * @since 2019/04/25
 */
public class ColaBeanInstantiationStrategy extends CglibSubclassingInstantiationStrategy{
    /**
     * Return an instance of the bean with the given name in this factory.
     * @param bd the bean definition
     * @param beanName the name of the bean when it's created in this context.
     * The name can be {@code null} if we're autowiring a bean which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @return a bean instance for this bean definition
     * @throws BeansException if the instantiation attempt failed
     */
    @Override
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner)
        throws BeansException{
        try{
            if(filterInstantiate(bd.getTargetType())){
                return newInstance(bd.getTargetType());
            }
            return super.instantiate(bd, beanName, owner);
        }catch (BeanInstantiationException e){
            return newInstance(bd.getTargetType());
        }
    }

    /**
     * Return an instance of the bean with the given name in this factory,
     * creating it via the given constructor.
     * @param bd the bean definition
     * @param beanName the name of the bean when it's created in this context.
     * The name can be {@code null} if we're autowiring a bean which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @param ctor the constructor to use
     * @param args the constructor arguments to apply
     * @return a bean instance for this bean definition
     * @throws BeansException if the instantiation attempt failed
     */
    @Override
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner,
                              Constructor<?> ctor, Object... args) throws BeansException{
        try{
            if(filterInstantiate(bd.getTargetType())){
                return newInstance(bd.getTargetType(), args);
            }
            return super.instantiate(bd, beanName, owner, ctor, args);
        }catch (BeanInstantiationException e){
            return newInstance(bd.getTargetType(), args);
        }
    }

    /**
     * Return an instance of the bean with the given name in this factory,
     * creating it via the given factory method.
     * @param bd the bean definition
     * @param beanName the name of the bean when it's created in this context.
     * The name can be {@code null} if we're autowiring a bean which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @param factoryBean the factory bean instance to call the factory method on,
     * or {@code null} in case of a static factory method
     * @param factoryMethod the factory method to use
     * @param args the factory method arguments to apply
     * @return a bean instance for this bean definition
     * @throws BeansException if the instantiation attempt failed
     */
    @Override
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner,
                              Object factoryBean, Method factoryMethod, Object... args) throws BeansException{
        try{
            if(filterInstantiate(bd.getTargetType())){
                return newInstance(bd.getTargetType(), args);
            }
            return super.instantiate(bd, beanName, owner, factoryBean,factoryMethod,  args);
        }catch (BeanInstantiationException e){
            return newInstance(bd.getTargetType(), args);
        }
    }

    private Object newInstance(Class clz, Object... arg){
        if(clz.getName().startsWith("org.mybatis.spring.mapper.MapperFactoryBean")){
            return new MapperFactoryBean((Class)arg[0]);
        }
        return MockHelper.newInstanceForMockDataProxy(clz);
    }

    private boolean filterInstantiate(Class clz){
        if(clz != null && (clz.getName().startsWith("com.taobao.tddl")
            || clz.getName().startsWith("com.taobao.tair")
            || clz.getName().startsWith("com.alibaba.middleware")
        || clz.getName().startsWith("org.mybatis.spring.mapper.MapperFactoryBean"))){
            return true;
        }
        return false;
    }
}
