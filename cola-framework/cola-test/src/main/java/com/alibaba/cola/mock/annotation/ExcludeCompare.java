package com.alibaba.cola.mock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExcludeCompare {
    String[] fields() default {};
    Class[] mockedInterfaces() default  {};
    String[] mockedMethods() default {};
}
