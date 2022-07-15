package com.alibaba.cola.statemachine.spring.annotation;

import java.lang.annotation.*;
/**
 * @author fulln
 * {@code @description} StateMachines annotation
 * @date  Created in  20:52  2022/7/14.
 **/
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface StateMachines {

    StateMachine[] value();
    /**
     * 是否覆盖默认的condition判断
     * @return false 不覆盖
     */
    boolean overrideDefaultCondition() default false;

}
