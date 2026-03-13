package com.alibaba.cola.unittest.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE, ElementType.METHOD})
public @interface ExpectRedis {
    /**
     * 测试校验数据路径, 通常放在测试夹具（fixture）下面
     */
    String value();

    /**
     * 重试等待key生效的间隔（ms）
     */
    long interval() default 200L;

    /**
     * 验证超时时间（ms）
     */
    long timeout() default 3000L;
}

