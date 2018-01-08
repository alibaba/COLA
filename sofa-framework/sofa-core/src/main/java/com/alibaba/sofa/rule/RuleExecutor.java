/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.rule;

import com.alibaba.sofa.extension.ExtensionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RuleExecutor
 *
 * Note that Rule is extensible as long as @Extension is added
 *
 * @author fulan.zjf 2017-11-04
 */
@Component
public class RuleExecutor extends ExtensionExecutor {

    @Autowired
    private PlainRuleRepository plainRuleRepository;

    @Override
    protected <C> C locateComponent(Class<C> targetClz) {
        C rule = (C) plainRuleRepository.getPlainRules().get(targetClz);
        return null != rule ? rule : super.locateComponent(targetClz);
    }

    public void validate(Class<? extends RuleI> targetClz, Object... candidate) {
        RuleI rule = this.locateComponent(targetClz);
        rule.validate(candidate);
    }
}
