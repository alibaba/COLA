package com.alibaba.cola.statemachine.builder;

/**
 * @author 龙也
 * @date 2022/9/15 12:02 PM
 */
@FunctionalInterface
public interface FailoverCallback<S, E, C> {

    /**
     * Callback function to execute on failover
     *
     * @param sourceState
     * @param event
     * @param context
     */
    void onFailover(S sourceState, E event, C context);
}
