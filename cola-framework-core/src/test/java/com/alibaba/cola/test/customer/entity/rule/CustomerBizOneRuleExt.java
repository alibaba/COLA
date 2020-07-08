package com.alibaba.cola.test.customer.entity.rule;

import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.entity.CustomerEntity;
import com.alibaba.cola.test.customer.entity.SourceType;

/**
 * CustomerBizOneRuleExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:10 PM
 */
@Extension(bizId = Constants.BIZ_1)
public class CustomerBizOneRuleExt implements CustomerRuleExtPt{

    @Override
    public boolean addCustomerCheck(CustomerEntity customerEntity) {
        if(SourceType.AD == customerEntity.getSourceType()){
            throw new BizException("Sorry, Customer from advertisement can not be added in this period");
        }
        return true;
    }
}
