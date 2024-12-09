package com.alibaba.cola.statemachine;

/**
 * CurrentStateFetcher is used to fetch the current state from the context
 *
 * @author Yanchi Zhang
 * @date 2024-12-09 11:21 AM
 */
public interface CurrentStateFetcher<S, C> {

    /**
     * Fetch the current state from the context
     *
     * @param context the context
     * @return the current state
     */
    S currentState(C context);
}
