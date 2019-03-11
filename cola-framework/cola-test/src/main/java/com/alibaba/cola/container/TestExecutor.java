package com.alibaba.cola.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.cola.mock.listener.ColaNotifier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Run test here
 * @author fulan.zjf 2017年10月27日 下午3:14:21
 */
public class TestExecutor {

    private ColaNotifier notifier = new ColaNotifier();
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
        //刷新
        //ColaMockito.g().refreshScanMockFromTest(testClz);
        runClassTest(testClz, testInstance);
    }
    
    public void testMethod() throws Exception {
        Class<?> testClz = Class.forName(className);
        Object testInstance = getTestInstance(testClz);
        //刷新
        //if(ColaMockito.g().getContext().isRecording()) {
        //    ColaMockito.g().refreshScanMockFromTest(testClz);
        //}
        runMethodTest(testClz, testInstance);
    }
    
    private void runMethodTest(Class<?> testClz, Object testInstance) throws Exception{
        Method[] allMethods = testClz.getMethods();
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
        Method method = testClz.getMethod(methodName);
        Description description = Description.createTestDescription(testClz, method.getName());
        notifier.fireTestRunStarted(testInstance);
        notifier.fireTestRunStarted(description);
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        notifier.fireTestStarted(method, description);
        //invoke test method
        invokeMethod(testInstance, method);
        notifier.fireTestFinished(method, description);
        //invoke after method
        invokeMethod(testInstance, afterMethod);
        notifier.fireTestRunFinished(description);
    }

    private Object getTestInstance(Class<?> testClz) throws Exception{
        if(testInstanceCache.get(className) != null) {
            return testInstanceCache.get(className);
        }
        Object testInstance = testClz.newInstance();
        injectWiredBean(testClz, testInstance);
        return testInstance;
    }

    private void runClassTest(Class<?> testClz, Object testInstance)throws Exception{
        Description description = Description.createSuiteDescription(testClz);
        Method[] allMethods = testClz.getMethods();
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
                    description.addChild(Description.createTestDescription(testClz, method.getName()));
                    break;
                }
            }
        }
        notifier.fireTestRunStarted(testInstance);
        notifier.fireTestRunStarted(description);
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        //invoke test methods
        for(Method testMethod: testMethods){
            notifier.fireTestStarted(testMethod, description);
            //testMethodStarted(testInstance, testMethod);
            invokeMethod(testInstance, testMethod);
            //testMethodFinished(testInstance, testMethod);
            notifier.fireTestFinished(testMethod, description);
        }
        //invoke after method
        invokeMethod(testInstance, afterMethod);
        notifier.fireTestRunFinished(description);
    }

    private static void invokeMethod(Object obj, Method method) throws Exception{
        if (method == null) {
            return;
        }
        method.invoke(obj);
    }
    
    private void injectWiredBean(Class<?> testClz, Object testInstance) {
       Field[] fields = testClz.getDeclaredFields();
       if(fields == null) {
           return;
       }
       for(Field field : fields) {
           String beanName = field.getName();
           Annotation autowiredAnn = field.getDeclaredAnnotation(Autowired.class);
           if (autowiredAnn == null) {
               System.out.println("Field "+beanName+" is not autowired, just ignore it");
               continue;
           }
           try {
               field.setAccessible(true);
               field.set(testInstance, context.getBean(beanName));
           } catch (IllegalArgumentException e){
               e.printStackTrace();
           }catch (BeansException | IllegalAccessException e) {
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

    public ColaNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(ColaNotifier notifier) {
        this.notifier = notifier;
    }
}