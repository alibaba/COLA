package com.alibaba.cola.ruleengine.core;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.api.Rules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRuleEngine implements RuleEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngine.class);
    private boolean isSorted = false;

    @Override
    public void fire(Rules rules, Facts facts) {
        if (rules.isEmpty()) {
            LOGGER.warn("No rules registered! Nothing to apply");
            return;
        }
        if(!isSorted){
            //for performance concern: only sort at the first time
            rules.sortRulesByPriority();
            isSorted = true;
        }
        for (Rule rule : rules) {
            rule.apply(facts);
        }
    }
}
