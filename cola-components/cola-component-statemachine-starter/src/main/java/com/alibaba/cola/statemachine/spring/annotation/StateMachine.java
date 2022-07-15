package com.alibaba.cola.statemachine.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fulln
 * {@code @description} annotation for state machine
 * @date  Created in  20:53  2022/7/14.
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(StateMachines.class)
public @interface StateMachine {

    String  from();

    String  to();

    String  on();

    String machineId();
}
