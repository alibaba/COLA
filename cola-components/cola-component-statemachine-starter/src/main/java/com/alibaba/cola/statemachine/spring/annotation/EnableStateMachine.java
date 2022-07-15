package com.alibaba.cola.statemachine.spring.annotation;


import com.alibaba.cola.statemachine.spring.config.StateMachineRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(StateMachineRegistrar.class)
public @interface EnableStateMachine {

}
