package com.alibaba.cola.ruleengine.fizzbuzz.v2;

@FunctionalInterface
public interface Action {
    String execute(int n);
}

