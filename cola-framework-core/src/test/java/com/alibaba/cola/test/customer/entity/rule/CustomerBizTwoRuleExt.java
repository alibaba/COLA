package com.alibaba.cola.test.customer.entity.rule;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.entity.CustomerEntity;

/**
 * CustomerBizTwoRuleExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:10 PM
 */
@Extension(bizId = Constants.BIZ_2)
public class CustomerBizTwoRuleExt implements CustomerRuleExtPt{

    @Override
    public boolean addCustomerCheck(CustomerEntity customerEntity) {
        //Any Customer can be added
        return true;
    }
}
