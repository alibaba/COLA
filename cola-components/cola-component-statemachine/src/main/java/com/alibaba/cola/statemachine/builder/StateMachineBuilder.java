package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.CurrentStateFetcher;
import com.alibaba.cola.statemachine.StateMachine;

/**
 * StateMachineBuilder
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:32 PM
 */
public interface StateMachineBuilder<S, E, C> {
    /**
     * Builder for one transition
     *
     * @return External transition builder
     */
    ExternalTransitionBuilder<S, E, C> externalTransition();

    /**
     * Builder for multiple transitions
     *
     * @return External transition builder
     */
    ExternalTransitionsBuilder<S, E, C> externalTransitions();
    /**
     * Builder for parallel transitions
     *
     * @return External transition builder
     */
    ExternalParallelTransitionBuilder<S, E, C> externalParallelTransition();

    /**
     * Start to build internal transition
     *
     * @return Internal transition builder
     */
    InternalTransitionBuilder<S, E, C> internalTransition();

    /**
     * set up fail callback, default do nothing {@code NumbFailCallbackImpl}
     *
     * @param callback
     */
    void setFailCallback(FailCallback<S, E, C> callback);

    /**
     * Set up current state fetcher
     *
     * @param fetcher the current state fetcher
     */
    void setCurrentStateFetcher(CurrentStateFetcher<S, C> fetcher);

    StateMachine<S, E, C> build(String machineId);

}
