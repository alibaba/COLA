package com.alibaba.cola.mock.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.utils.MockHelper;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class FactoryBeanProxy implements MethodInterceptor,InvocationHandler {
    private Class<?> mapperInterface;
    private Object instance;
    private String beanName;
    private boolean online = false;

    public FactoryBeanProxy(Class mapperInterface, Object instance, String beanName, boolean online){
        this.mapperInterface = mapperInterface;
        this.instance = instance;
        this.beanName = beanName;
        this.online = online;
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
        //实现处理
        if(instance instanceof FactoryBean){
            if("getObject".equals(method.getName())){
                Object target = ((FactoryBean)instance).getObject();
                Class targetType = ((FactoryBean)instance).getObjectType();
                DataRecordProxy mapperProxy;
                if(online){
                    mapperProxy = new OnlineDataRecordProxy(targetType, target);
                }else{
                    mapperProxy = new DataRecordProxy(targetType, target);
                }

                //Enhancer enhancer = new Enhancer();
                //enhancer.setSuperclass(this.mapperInterface);
                //enhancer.setCallback(mapperProxy);
                //Object proxy = enhancer.create();

                Object proxy = MockHelper.createMockFor(this.mapperInterface, mapperProxy);

                //MainRecordProxy mainRecordProxy = new MainRecordProxy(targetType, proxy);
                //proxy = ColaMockito.g().createMockFor(this.mapperInterface, mainRecordProxy);

                ColaMockito.g().getContext().putMonitorMock(new MockServiceModel(this.mapperInterface, beanName, target, proxy));
                return proxy;
            }
        }
        method.setAccessible(true);
        result = method.invoke(instance, objects);
        return result;
    }
}
