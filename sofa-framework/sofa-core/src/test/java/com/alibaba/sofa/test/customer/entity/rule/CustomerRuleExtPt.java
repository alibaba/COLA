package com.alibaba.sofa.test.customer.entity.rule;

import com.alibaba.sofa.extension.ExtensionPointI;
import com.alibaba.sofa.rule.RuleI;
import com.alibaba.sofa.test.customer.entity.CustomerEntity;

/**
 * CustomerRuleExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:03 PM
 */
public interface CustomerRuleExtPt extends RuleI, ExtensionPointI{

    //Different business check for different biz
    public boolean addCustomerCheck(CustomerEntity customerEntity);

    //Different upgrade policy for different biz
    default public void customerUpgradePolicy(CustomerEntity customerEntity){
        //Nothing special
    }
}
