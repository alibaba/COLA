package com.alibaba.cola.event;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface EventHandler {
}
