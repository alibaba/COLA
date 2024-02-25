package com.alibaba.cola.ruleengine.api;

@FunctionalInterface
public interface Action {
    void execute(Facts facts);
}

