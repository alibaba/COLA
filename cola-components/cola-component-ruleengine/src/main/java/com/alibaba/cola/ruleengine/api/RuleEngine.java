package com.alibaba.cola.ruleengine.api;

public interface RuleEngine {
    /**
     * Fire all registered rules on given facts.
     */
    void fire(Rules rules, Facts facts);

}
