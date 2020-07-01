package com.alibaba.cola.test.customer.entity.rule;

import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.test.customer.entity.CustomerEntity;

/**
 * CustomerRuleExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:03 PM
 */
public interface CustomerRuleExtPt extends  ExtensionPointI{

    //Different business check for different biz
    public boolean addCustomerCheck(CustomerEntity customerEntity);

    //Different upgrade policy for different biz
    default public void customerUpgradePolicy(CustomerEntity customerEntity){
        //Nothing special
    }
}
