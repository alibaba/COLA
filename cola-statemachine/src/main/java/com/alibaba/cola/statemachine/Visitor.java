package com.alibaba.cola.statemachine;

/**
 * Visitor
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:41 PM
 */
public interface Visitor {
    /**
     * @param visitable the element to be visited.
     */
    void visitOnEntry(StateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     */
    void visitOnExit(StateMachine<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     */
    void visitOnEntry(State<?, ?, ?> visitable);

    /**
     * @param visitable the element to be visited.
     */
    void visitOnExit(State<?, ?, ?> visitable);
}
