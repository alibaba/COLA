package com.alibaba.cola.mock.utils.reflection;

import java.lang.reflect.Field;

/**
 * @author shawnzhan.zxy
 * @since 2019/01/20
 */
public class BeanPropertySetter {

    private String fieldName;
    private Object target;
    private Field field;

    public BeanPropertySetter(Object target, String fieldName){
        this.fieldName = fieldName;
        this.target = target;
        this.field = findField();
    }


    public boolean isFieldExists(){
        if(field != null){
            return true;
        }
        return false;
    }

    public void setValue(Object value){
        if(!isFieldExists()){
            return;
        }
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象中的字段
     * @return the field you want to  find
     * @throws Throwable
     * @throws NoSuchFieldException
     */
    public Field findField() {
        Class clzz = this.target.getClass();
        Field[] fields = clzz.getDeclaredFields();
        Field dest = null;
        while (!hasField(fields,fieldName) && !clzz.getName().equalsIgnoreCase("java.lang.Object")) {
            clzz = clzz.getSuperclass();
            fields = clzz.getDeclaredFields();
        }
        if (!hasField(fields,fieldName)) {
            return dest;
        }
        try {
            dest = clzz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public<T> T getValue() {
        field.setAccessible(true);
        try {
            return (T)field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Field getField() {
        return field;
    }

    /**
     * 判断对象中是否有要找的字段
     * @param fields the fields which you want to find
     * @param fieldName the field name you want to find
     * @return if the field in field return true else return false
     */
    private boolean hasField(Field[] fields, String fieldName) {
        for (int i = 0; i < fields.length ;i ++) {
            if (fields[i].getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
}
