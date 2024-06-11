package com.alibaba.cola.statemachine;

import java.util.List;

/**
 * StateChainVisitor
 *
 * @author benym
 * @date 2024/6/12 18:06
 */
public interface StateChainVisitor {

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnEntry(StateChain<?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     * @return String
     */
    String visitOnExit(StateChain<?, ?> visitable);

    /**
     * @param chainStateList the element to be visited.
     * @param event event
     * @return String
     */
    <S, C, E> String visitOnStateChain(List<State<S,E,C>> chainStateList, E event);
}
