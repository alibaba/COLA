package com.alibaba.cola.ruleengine.api;

public interface RuleEngine {
    /**
     * Fire rule on given facts.
     */
    void fire(Rule rule, Facts facts);

}
