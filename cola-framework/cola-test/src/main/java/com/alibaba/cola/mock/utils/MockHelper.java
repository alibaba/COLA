package com.alibaba.cola.mock.utils;

import java.util.HashMap;
import java.util.Map;

import org.objenesis.ObjenesisStd;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class MockHelper {
    private static ObjenesisStd objenesis = new ObjenesisStd(true);

    private static Map<String, String> notMonitorMethod = new HashMap<>();
    {
        notMonitorMethod.put("toString", "toString");
        notMonitorMethod.put("hashCode", "hashCode");
        notMonitorMethod.put("equals", "equals");
    }

    public static boolean isMonitorMethod(String methodName){
        return notMonitorMethod.get(methodName) == null;
    }

    public static Class createMockClass(Class clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setUseFactory(true);
        enhancer.setNamingPolicy(ColaNamingPolicy.INSTANCE);
        enhancer.setSerialVersionUID(42L);
        enhancer.setSuperclass(clazz);
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class});
        return enhancer.createClass();
    }

    public static Object createMockFor(Class clazz, MethodInterceptor interceptor){
        Class proxyCls = createMockClass(clazz);
        Factory proxy = (Factory)newInstance(proxyCls);
        proxy.setCallbacks(new Callback[] {interceptor});
        return proxy;
    }

    public static Object newInstance(Class clazz){
        return objenesis.newInstance(clazz);
    }

    public static Object newInstance(String clazz){
        try {
            Class cls = Class.forName(clazz);
            return objenesis.newInstance(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
