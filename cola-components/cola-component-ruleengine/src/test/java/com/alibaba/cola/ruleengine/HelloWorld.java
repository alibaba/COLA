package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.core.DefaultRuleEngine;
import com.alibaba.cola.ruleengine.core.RuleBuilder;

public class HelloWorld {
    public static void main(String[] args) {
        RuleEngine ruleEngine = new DefaultRuleEngine();
        Rule rule = new RuleBuilder()
                .name("hello world rule")
                .description("always say hello world")
                .priority(1)
                .when(facts -> true)
                .then(facts -> System.out.println("hello world"))
                .build();

        ruleEngine.fire(rule, null);
    }
}
