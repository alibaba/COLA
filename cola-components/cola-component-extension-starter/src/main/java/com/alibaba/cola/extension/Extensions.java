package com.alibaba.cola.extension;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * because {@link Extension} only supports single coordinates,
 * this annotation is a supplement to {@link Extension} and supports multiple coordinates
 *
 * @author wangguoqiang wrote on 2022/10/10 12:19
 * @version 1.0
 * @see Extension
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Extensions {

    String[] bizId() default BizScenario.DEFAULT_BIZ_ID;

    String[] useCase() default BizScenario.DEFAULT_USE_CASE;

    String[] scenario() default BizScenario.DEFAULT_SCENARIO;

    Extension[] value() default {};

}
