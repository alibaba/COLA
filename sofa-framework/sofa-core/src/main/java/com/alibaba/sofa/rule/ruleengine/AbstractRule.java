package com.alibaba.sofa.rule.ruleengine;

import com.alibaba.sofa.rule.RuleI;

/**
 * 业务规则的抽象实现，可用于组合规则
 * @author xueliang.sxl
 *
 */
public abstract class AbstractRule implements RuleI {

	public RuleI and(RuleI other) {
		return new AndRule(this, other);
	}

	public RuleI or(RuleI other) {
		return new OrRule(this, other);
	}

	public RuleI not() {
		return new NotRule(this);
	}

}
