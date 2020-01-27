package com.alibaba.cola.executor;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Step 在处理复杂Command时候，可以分成多个阶段（Phase）处理，每个阶段可以分成多个步骤（Step）。
 *
 * @author Frank Zhang
 * @date 2019-07-29 18:36
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Step {
}
