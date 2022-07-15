package com.alibaba.cola.statemachine.spring.config;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class AbstractConditionAdapter<C> {

    /**
     * 自己的根据枚举简单的实现Condition判断
     * @param from
     * @param to
     * @param c
     * @return
     */
    public abstract boolean commonCheck(Enum from, Enum to, C c);

    public Class<C> getTargetName() {
        try {
            return (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            log.warn("转换出现异常",e);
        }
        return null;
    }
}
