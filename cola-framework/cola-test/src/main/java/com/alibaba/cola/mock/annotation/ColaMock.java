package com.alibaba.cola.mock.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @author shawnzhan.zxy
 * @since 2018/08/30
 */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColaMock {
}
