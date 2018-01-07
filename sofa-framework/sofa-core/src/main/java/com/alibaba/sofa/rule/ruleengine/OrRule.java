package com.alibaba.sofa.rule.ruleengine;

import com.alibaba.sofa.rule.RuleI;

/**
 * 组合Or的业务规则
 * @author xueliang.sxl
 *
 */
public class OrRule extends AbstractRule {
	RuleI one;
	RuleI other;

	public OrRule(RuleI x, RuleI y){
		one = x;
		other = y;
	}

	@Override
	public boolean check(Object candidate) {
		return one.check(candidate) || other.check(candidate);
	}

}