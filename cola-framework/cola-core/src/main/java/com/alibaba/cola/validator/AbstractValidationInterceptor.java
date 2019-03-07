package com.alibaba.cola.validator;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;

/**
 * AbstractValidationInterceptor
 *
 * @author Frank Zhang
 * @date 2019-03-02 4:44 PM
 */
public abstract class AbstractValidationInterceptor {

    /**
     * Recursively do validation to the target if the field is not primitive or wrapped.
     *
     * @param target
     */
    protected void validate(Object target){
        doValidation(target);
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields){
            if(isChildField(field.getType())){
                continue;
            }
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(target);
            } catch (IllegalAccessException e) {
                continue;
            }

            //Recursively validate
            validate(fieldValue);
        }
    }

    private boolean isChildField(Class type){
        return type.equals(String.class) || ClassUtils.isPrimitiveOrWrapper(type);
    }

    abstract protected void doValidation(Object target);
}
