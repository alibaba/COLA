package com.alibaba.cola.ruleengine.core;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * This is Natural Rules
 */
public class NaturalRules extends CompositeRule{
    private static final Logger LOGGER = LoggerFactory.getLogger(NaturalRules.class);

    public static CompositeRule of(Rule... rules) {
        CompositeRule instance = new NaturalRules();
        Collections.addAll(instance.rules, rules);
        return instance;
    }

    @Override
    public boolean evaluate(Facts facts) {
        //不支持, which means Natural Rules can not be the children of other rules
        throw new RuntimeException("evaluate not supported for natural composite");
    }

    @Override
    public void execute(Facts facts) {
        //不支持, which means Natural Rules can not be the children of other rules
        throw new RuntimeException("execute not supported for natural composite");
    }

    @Override
    protected boolean doApply(Facts facts) {
        LOGGER.debug("start Natural composite rule apply");
        for (Rule rule : rules) {
            rule.apply(facts);
        }
        return true;
    }
}
