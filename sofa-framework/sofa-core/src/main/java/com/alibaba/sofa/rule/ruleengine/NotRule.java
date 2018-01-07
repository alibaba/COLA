package com.alibaba.sofa.rule.ruleengine;

import com.alibaba.sofa.rule.RuleI;

/**
 * 组合Not的业务规则
 * @author xueliang.sxl
 *
 */
public class NotRule extends AbstractRule {
	RuleI wrapped;

	public NotRule(RuleI x){
		wrapped = x;
	}

	@Override
	public boolean check(Object candidate) {
		return !wrapped.check(candidate);
	}

}