package com.alibaba.cola.ruleengine.api;

import java.util.Objects;

@FunctionalInterface
public interface Condition {

    boolean evaluate(Facts facts);

    //谓词and逻辑，参考Predicate
    default Condition and(Condition other) {
        Objects.requireNonNull(other);
        return (facts) -> {
            return this.evaluate(facts) && other.evaluate(facts);
        };
    }

    //谓词or逻辑，参考Predicate
    default Condition or(Condition other) {
        Objects.requireNonNull(other);
        return (facts) -> {
            return this.evaluate(facts) || other.evaluate(facts);
        };
    }

    /**
     * A NoOp {@link Condition} that always returns false.
     */
    Condition FALSE = facts -> false;

    /**
     * A NoOp {@link Condition} that always returns true.
     */
    Condition TRUE = facts -> true;
}

