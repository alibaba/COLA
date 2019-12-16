package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostInterceptor {

    Class<? extends Command>[] commands() default {};

}
