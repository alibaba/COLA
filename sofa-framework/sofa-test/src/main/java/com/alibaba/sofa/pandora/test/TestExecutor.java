package com.alibaba.sofa.pandora.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Run test here
 * @author fulan.zjf 2017年10月27日 下午3:14:21
 */
public class TestExecutor {
    
    private String className;
    private String methodName;
    
    private Map<String, Object> testInstanceCache = new HashMap<String, Object>();
    
    private ApplicationContext context;

    public TestExecutor(ApplicationContext context){
        this.context = context;
    }

    public void testClass() throws Exception {
        Class<?> testClz = Class.forName(className);
        Object testInstance = getTestInstance(testClz);
        runClassTest(testClz, testInstance);
    }
    
    public void testMethod() throws Exception {
        Class<?> testClz = Class.forName(className);
        Object testInstance = getTestInstance(testClz);    
        runMethodTest(testClz, testInstance);
    }
    
    private void runMethodTest(Class<?> testClz, Object testInstance) throws Exception{
        Method[] allMethods = testClz.getDeclaredMethods();
        Method beforeMethod = null;
        Method afterMethod = null;
        for (Method method : allMethods){
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof Before){
                    beforeMethod = method;
                    break;
                }
                if(annotation instanceof After){
                    afterMethod = method;
                    break;
                }
            }
        }
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        //invoke test method
        invokeMethod(testInstance, testClz.getMethod(methodName));
        //invoke after method
        invokeMethod(testInstance, afterMethod);        
    }

    private Object getTestInstance(Class<?> testClz) throws Exception{
        if(testInstanceCache.get(className) != null)
            return testInstanceCache.get(className);
        Object testInstance = testClz.newInstance();
        injectWiredBean(testClz, testInstance);
        return testInstance;
    }

    private void runClassTest(Class<?> testClz, Object testInstance)throws Exception{
        Method[] allMethods = testClz.getDeclaredMethods();
        Method beforeMethod = null;
        Method afterMethod = null;
        List<Method> testMethods = new ArrayList<Method>();
        for (Method method : allMethods){
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof Before){
                    beforeMethod = method;
                    break;
                }
                if(annotation instanceof After){
                    afterMethod = method;
                    break;
                }
                if(annotation instanceof Test || method.getName().startsWith("test")){
                    testMethods.add(method);
                    break;
                }
            }
        }   
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        //invoke test methods
        for(Method testMethod: testMethods){
            invokeMethod(testInstance, testMethod);
        }
        //invoke after method
        invokeMethod(testInstance, afterMethod);
    }

    private static void invokeMethod(Object obj, Method method) throws Exception{
        if (method == null)
            return;
        method.invoke(obj);
    }
    
    private void injectWiredBean(Class<?> testClz, Object testInstance) {
       Field[] fields = testClz.getDeclaredFields();
       if(fields == null) return;
       for(Field field : fields) {
           String beanName = field.getName();
           Annotation autowiredAnn = field.getDeclaredAnnotation(Autowired.class);
           if (autowiredAnn == null) {
               //System.out.println("Field "+beanName+" is not autowired, just ignore it");
               return;
           }
           try {
               field.setAccessible(true);
               field.set(testInstance, context.getBean(beanName));
           } catch (BeansException | IllegalArgumentException | IllegalAccessException e) {
               //try to use type to get bean
               try {
                field.set(testInstance, context.getBean(field.getType()));
                } catch (Exception innerE) {
                    System.err.println("oops!!! "+beanName + " can not be injected to "+ className);
                    System.err.println(innerE.getMessage());
                } 
           }
       }
    }

    
    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    
    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    
    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    
    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    
}