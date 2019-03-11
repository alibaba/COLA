package com.alibaba.cola.mock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface ColaMockConfig {
    Class[] mocks() default {};
    String[] regexMocks() default {};
    Class[] annotationMocks() default {};
}
