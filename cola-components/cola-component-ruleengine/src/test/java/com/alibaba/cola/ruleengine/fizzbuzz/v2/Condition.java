package com.alibaba.cola.ruleengine.fizzbuzz.v2;

import java.util.Objects;

@FunctionalInterface
public interface Condition {

    boolean evaluate(int n);

    //谓词and逻辑，参考Predicate
    default Condition and(Condition other) {
        Objects.requireNonNull(other);
        return (n) -> {
            return this.evaluate(n) && other.evaluate(n);
        };
    }

    //谓词or逻辑，参考Predicate
    default Condition or(Condition other) {
        Objects.requireNonNull(other);
        return (n) -> {
            return this.evaluate(n) || other.evaluate(n);
        };
    }
}
