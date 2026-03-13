package com.alibaba.cola.unittest.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试启动时注入redis记录
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE, ElementType.METHOD})
public @interface SetupRedis {
    /**
     * 测试准备数据路径, 通常放在测试夹具（fixture）下面，比如：/fixture/job.json
     */
    String value();
}
