package com.alibaba.cola.ruleengine.core;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;

import java.util.Collections;

public class AnyRules extends CompositeRule{

    public static CompositeRule anyOf(Rule... rules) {
        CompositeRule instance = new AnyRules();
        Collections.addAll(instance.rules, rules);
        return instance;
    }

    @Override
    public boolean evaluate(Facts facts) {
        if (rules.stream().anyMatch(rule -> rule.evaluate(facts)))
            return true;
        return false;
    }

    @Override
    public void execute(Facts facts) {
        //不支持OR relation
        throw new RuntimeException("execute not supported for OR relation composite");
    }

    @Override
    protected boolean doApply(Facts facts) {
        for (Rule rule : rules) {
            //短路操作，只要第一个rule成功执行，其它就不执行了
            if (rule.apply(facts)) {
                return true;
            }
        }
        return false;
    }
}
