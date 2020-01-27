package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreInterceptor {

    Class<? extends Executor>[] commands() default {};

}
