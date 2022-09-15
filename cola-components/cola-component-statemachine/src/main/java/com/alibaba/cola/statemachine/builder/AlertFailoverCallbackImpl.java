package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.exception.TransitionFailoverException;

/**
 * Default failover callback, do nothing.
 *
 * @author 龙也
 * @date 2022/9/15 12:02 PM
 */
public class AlertFailoverCallbackImpl<S, E, C> implements FailoverCallback<S, E, C> {

    @Override
    public void onFailover(S sourceState, E event, C context) {
        throw new TransitionFailoverException(
            "Cannot fire event [" + event + "] on current state [" + sourceState + "] with context [" + context + "]");
    }
}
