package com.alibaba.cola.domain;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * ApplicationContextHelper
 *
 * @author Frank Zhang
 * @date 2020-11-14 1:58 PM
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> targetClz) {
        T beanInstance = null;
        //优先按type查
        try {
            beanInstance = (T) applicationContext.getBean(targetClz);
        } catch (Exception e) {
        }
        //按name查
        if (beanInstance == null) {
            String simpleName = targetClz.getSimpleName();
            //首字母小写
            simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            beanInstance = (T) applicationContext.getBean(simpleName);
        }
        if (beanInstance == null) {
            throw new RuntimeException("Component " + targetClz + " can not be found in Spring Container");
        }
        return beanInstance;
    }

    public static <T> T getAggregateBean(Class<T> targetClz) {
        T beanInstance = null;
        beanInstance = getInstance(targetClz);
        beanFieldsInstance(beanInstance, targetClz);
        return beanInstance;
    }


    public static Object getBean(String claz) {
        return ApplicationContextHelper.applicationContext.getBean(claz);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ApplicationContextHelper.applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... params) {
        return ApplicationContextHelper.applicationContext.getBean(requiredType, params);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private static <T> T getInstance(Class<T> targetClz) {
        if (targetClz.getAnnotation(Entity.class) != null) {
            return (T) applicationContext.getBean(targetClz);
        } else if (targetClz.getSimpleName().endsWith("V") || targetClz.getSimpleName().endsWith("E")) {
            try {
                return targetClz.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return null;
    }

    private static <T> void beanFieldsInstance(T beanInstance, Class<T> targetClz) {
        Field[] fields = targetClz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            try {
                if (isAggregateSupportType(fieldType) && field.get(beanInstance) == null) {
                    T aggregateBean = (T) getAggregateBean(fieldType);
                    field.set(beanInstance, aggregateBean);
                } else if (fieldType.getName().equals("java.util.List")) {
                    field.set(beanInstance, new ArrayList<T>());
                } else if (fieldType.getName().equals("java.util.Set")) {
                    field.set(beanInstance, new HashSet<T>());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        });
    }

    private static boolean isAggregateSupportType(Class<?> targetClz) {
        return targetClz.getAnnotation(Entity.class) != null || targetClz.getSimpleName().endsWith("V") || targetClz.getSimpleName().endsWith("E");
    }

}
