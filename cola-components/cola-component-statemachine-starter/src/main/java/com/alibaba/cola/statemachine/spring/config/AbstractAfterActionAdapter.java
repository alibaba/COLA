package com.alibaba.cola.statemachine.spring.config;


import com.alibaba.cola.statemachine.Action;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class AbstractAfterActionAdapter<S, E, C> implements Action<S, E, C> {

    /**
     * 判断要在哪个Action中实现
     *
     * @param from
     * @param to
     * @param on  condition
     * @param m
     * @return
     */
    public abstract boolean ifUsedInCurrentAction(S from, S to, E on, C m);

    public Class<C> getTargetName() {
        try {
            Class<C> actualTypeArgument = (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
            return actualTypeArgument;
        } catch (Exception e) {
            log.warn("转换出现异常", e);
        }
        return null;
    }
}
