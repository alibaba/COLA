package com.alibaba.cola.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.cola.container.command.TestClassRunCmd;
import com.alibaba.cola.container.command.TestMethodRunCmd;
import com.alibaba.cola.mock.listener.ColaNotifier;
import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.utils.reflection.BeanMetaUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static com.alibaba.cola.mock.model.ColaTestDescription.createTestDescription;

/**
 * Run test here
 * @author fulan.zjf 2017年10月27日 下午3:14:21
 */
public class TestExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TestExecutor.class);
    private ColaNotifier notifier = new ColaNotifier();
    private String className;
    private String methodName;
    
    private Map<String, Object> testInstanceCache = new HashMap<String, Object>();
    
    private ApplicationContext context;

    public TestExecutor(ApplicationContext context){
        this.context = context;
    }

    public void execute(TestClassRunCmd cmd) throws Exception {
        setClassName(cmd.getClassName());

        Class<?> testClz = Class.forName(className);
        Object testInstance = getTestInstance(testClz);
        runClassTest(cmd, testClz, testInstance);
    }
    
    public void execute(TestMethodRunCmd cmd) throws Exception {
        setClassName(cmd.getClassName());
        setMethodName(cmd.getMethodName());

        Class<?> testClz = Class.forName(className);
        Object testInstance = getTestInstance(testClz);
        runMethodTest(cmd, testClz, testInstance);
    }
    
    private void runMethodTest(TestMethodRunCmd cmd, Class<?> testClz, Object testInstance) throws Exception{
        Method beforeMethod = BeanMetaUtils.findMethod(testClz, Before.class);
        Method afterMethod = BeanMetaUtils.findMethod(testClz, After.class);
        Method method = testClz.getMethod(methodName);

        ColaTestDescription colaDes = ColaTestDescription.createTestDescription(cmd, testInstance, method);
        notifier.fireTestRunStarted(colaDes);
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        notifier.fireTestStarted(method, colaDes.getDescription());
        //invoke test method
        invokeMethod(testInstance, method);
        notifier.fireTestFinished(method, colaDes.getDescription());
        //invoke after method
        invokeMethod(testInstance, afterMethod);
        notifier.fireTestRunFinished(colaDes.getDescription());
    }

    private Object getTestInstance(Class<?> testClz) throws Exception{
        if(testInstanceCache.get(className) != null) {
            return testInstanceCache.get(className);
        }
        Object testInstance = testClz.newInstance();
        injectWiredBean(testClz, testInstance);
        return testInstance;
    }

    private void runClassTest(TestClassRunCmd cmd, Class<?> testClz, Object testInstance)throws Exception{
        ColaTestDescription colaDes = ColaTestDescription.createTestDescription(cmd, testInstance);
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
                    colaDes.getDescription().addChild(Description.createTestDescription(testClz, method.getName()));
                    break;
                }
            }
        }
        //notifier.fireTestRunStarted(testInstance, command);

        notifier.fireTestRunStarted(colaDes);
        //invoke before method
        invokeMethod(testInstance, beforeMethod);
        //invoke test methods
        for(Method testMethod: testMethods){
            notifier.fireTestStarted(testMethod, colaDes.getDescription());
            //testMethodStarted(testInstance, testMethod);
            invokeMethod(testInstance, testMethod);
            //testMethodFinished(testInstance, testMethod);
            notifier.fireTestFinished(testMethod, colaDes.getDescription());
        }
        //invoke after method
        invokeMethod(testInstance, afterMethod);
        notifier.fireTestRunFinished(colaDes.getDescription());
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
               continue;
           }
           trySetFieldValue(field, testInstance, beanName);
       }
    }

    private void trySetFieldValue(Field field, Object testInstance, String beanName){
        try {
            field.setAccessible(true);
            field.set(testInstance, context.getBean(beanName));
            return;
        } catch (IllegalArgumentException e){
            if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().indexOf("\\$Proxy") > 0){
                logger.error("此错误一般是实际类被代理导致，请尝试把字段类型改为接口!", e);
                throw e;
            }
        }catch (BeansException | IllegalAccessException e) {
            logger.warn("根据beanName查找失败，尝试byType查找");
        }

        try {
            field.set(testInstance, context.getBean(field.getType()));
        } catch (Exception innerE) {
            logger.error("oops!!! "+beanName + " can not be injected to "+ className, innerE);
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