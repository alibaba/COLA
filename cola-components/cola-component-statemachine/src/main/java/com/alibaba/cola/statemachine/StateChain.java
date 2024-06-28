package com.alibaba.cola.statemachine;

import java.util.List;

/**
 * StateChain
 *
 * @author benym
 * @date 2024/6/11 23:15
 */
public interface StateChain<S, E> extends StateChainVisitable {

    /**
     * Gets the source state of stateChain
     *
     * @return the source state
     */
    S getSource();

    /**
     * Gets the list of target states for stateChain
     *
     * @return the list of target states
     */
    List<S> getTargets();

    /**
     * Gets the event of stateChain
     *
     * @return event
     */
    E getEvent();

    /**
     * Print stateChain from the console
     */
    void showStateChain();

    /**
     * Generate the plantUml diagram of stateChain
     *
     * @return String
     */
    String generatePlantUml();
}
