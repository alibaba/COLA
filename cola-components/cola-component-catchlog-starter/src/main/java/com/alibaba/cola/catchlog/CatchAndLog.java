package com.alibaba.cola.catchlog;

/**
 * CatchAndLog
 *
 * @author Frank Zhang
 * @date 2020-11-10 10:48 AM
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CatchAndLog {

}
