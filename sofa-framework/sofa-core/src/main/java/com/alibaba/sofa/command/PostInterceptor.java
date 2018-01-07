package com.alibaba.sofa.command;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostInterceptor {

    Class<? extends com.alibaba.sofa.dto.Command>[] commands() default {};

}
