package com.alibaba.cola.statemachine.builder;

/**
 * Default failover callback, do nothing.
 *
 * @author 龙也
 * @date 2022/9/15 12:02 PM
 */
public class NumbFailoverCallbackImpl<S, E, C> implements FailoverCallback<S, E, C> {

    @Override
    public void onFailover(S sourceState, E event, C context) {
        //do nothing
    }
}
