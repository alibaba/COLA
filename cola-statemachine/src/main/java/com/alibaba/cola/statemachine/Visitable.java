package com.alibaba.cola.statemachine;

/**
 * Visitable
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:41 PM
 */
public interface Visitable {
    String accept(final Visitor visitor);
}
