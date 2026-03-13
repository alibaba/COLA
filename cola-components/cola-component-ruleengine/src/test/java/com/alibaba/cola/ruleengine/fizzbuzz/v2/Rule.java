package com.alibaba.cola.ruleengine.fizzbuzz.v2;

@FunctionalInterface
public interface Rule {
    String apply(int n);
}
