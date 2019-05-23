package com.alibaba.cola.mock.proxy;

import java.lang.reflect.Method;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.utils.MockHelper;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * 弃用
 *    对于transcation类 继承、接口 有些方法private/final好像并不能生成代理方法，导致proxy 始终报nosuchmethod
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class MainRecordProxy implements MethodInterceptor {
    private Class<?> mapperInterface;
    private Object instance;

    public MainRecordProxy(Class mapperInterface, Object instance){
        this.mapperInterface = mapperInterface;
        this.instance = instance;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return invokeMethod(o, method, objects, methodProxy);
    }

    public Object invokeMethod(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        method.setAccessible(true);
        if(ColaMockito.g().getCurrentTestModel() != null
            && ColaMockito.g().getCurrentTestModel().getTestClazz().isAssignableFrom(mapperInterface)){
            ColaMockito.g().getFileDataEngine().clean();
        }
        Method oriMethod = instance.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        result = oriMethod.invoke(instance, objects);
        //result = method.invoke(instance, objects);
        if(!MockHelper.isMonitorMethod(method.getName())){
            return result;
        }
        //if(mapperInterface.getName().indexOf("CustomerStatHandler") > 0){
        //    System.out.println("===" + mapperInterface);
        //}

        if(ColaMockito.g().getCurrentTestModel() != null
            && ColaMockito.g().getCurrentTestModel().getTestClazz().isAssignableFrom(mapperInterface)){
            ColaMockito.g().getFileDataEngine().flushOutputData();
            ColaMockito.g().getFileDataEngine().flushInputParamsFile();
        }
        return result;
    }


}
