package com.alibaba.sofa.boot;

import com.alibaba.sofa.rule.PlainRuleRepository;
import com.alibaba.sofa.rule.RuleI;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Plain Rule is Rule Component without ExtensionPoint
 * @author fulan.zjf
 * @date 2017/12/21
 */
@Component
public class PlainRuleRegister implements RegisterI, ApplicationContextAware {

    @Autowired
    private PlainRuleRepository plainRuleRepository;

    private ApplicationContext applicationContext;

    @Override
    public void doRegistration(Class<?> targetClz) {
        RuleI plainRule = (RuleI) applicationContext.getBean(targetClz);
        plainRuleRepository.getPlainRules().put(plainRule.getClass(), plainRule);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
