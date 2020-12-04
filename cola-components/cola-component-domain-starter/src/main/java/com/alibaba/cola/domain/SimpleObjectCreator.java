package com.alibaba.cola.domain;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author liuziyuan
 * @date 12/4/2020 1:36 PM
 */
public class SimpleObjectCreator {

//    public static <T> T getAggregateBean(Class<T> targetClz) {
//        T beanInstance = null;
//        beanInstance = (T) applicationContext.getBean(targetClz);
//        if (beanInstance == null){
//            try {
//                beanInstance = targetClz.getConstructor().newInstance();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        instanceFields(beanInstance, targetClz);
//        return beanInstance;
//    }
//
//    private static <T> T  instanceFields(T beanInstance, Class<T> targetClz){
//        Field[] fields = targetClz.getDeclaredFields();
//        Arrays.stream(fields).forEach(field -> {
//            field.setAccessible(true);
//            Class<?> fieldType = field.getType();
//            try {
//                if (fieldType.getAnnotation(Entity.class) != null && field.get(beanInstance) == null) {
//                    Object aggregateBean = getAggregateBean(fieldType);
//                    if (aggregateBean == null){
//                        aggregateBean = applicationContext.getBean(fieldType);
//                    }
//                    field.set(beanInstance, aggregateBean);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            field.setAccessible(false);
//        });
//        return beanInstance;
//    }
}
