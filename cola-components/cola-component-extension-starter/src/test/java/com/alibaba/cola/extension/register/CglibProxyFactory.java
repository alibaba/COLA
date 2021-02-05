package com.alibaba.cola.extension.register;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibProxyFactory {

    public static <T> T createProxy(T object) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(new ProxyCallback(object));
        return (T) enhancer.create();
    }

    public static class ProxyCallback implements MethodInterceptor {

        private Object target;
    
        public ProxyCallback(Object target) {
            this.target = target;
        }
        
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("ProxyObject::before");
            Object object = proxy.invoke(target, args);
            System.out.println("ProxyObject::after");
            return object;
        }
    }
}
