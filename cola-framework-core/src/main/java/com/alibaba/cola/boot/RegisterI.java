package com.alibaba.cola.boot;

import java.lang.annotation.Annotation;

/**
 * register
 *
 * @author fattyca1
 */
public interface RegisterI<T>{

    /**
     * do registration
     * @param registrationObj registration Obj
     */
    void doRegistration(T registrationObj);

    /**
     * What annotations can be registered
     * @return annotation
     */
    Class<? extends Annotation> registrationAnnotation();
}
