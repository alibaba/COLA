package com.alibaba.sofa.test.customer.validator.extension;

import com.alibaba.sofa.exception.BizException;
import com.alibaba.sofa.extension.Extension;
import com.alibaba.sofa.test.customer.AddCustomerCmd;
import com.alibaba.sofa.test.customer.Constants;
import com.alibaba.sofa.test.customer.CustomerType;
import com.alibaba.sofa.test.customer.validator.extensionpoint.AddCustomerValidatorExtPt;

/**
 * AddCustomerBizOneValidator
 *
 * @author Frank Zhang
 * @date 2018-01-07 1:31 AM
 */
@Extension(bizCode = Constants.BIZ_1)
public class AddCustomerBizOneValidator implements AddCustomerValidatorExtPt{

    @Override
    public void validate(Object candidate) {
        AddCustomerCmd addCustomerCmd = (AddCustomerCmd) candidate;
        //For BIZ TWO CustomerTYpe could not be VIP
        if(CustomerType.VIP == addCustomerCmd.getCustomerCO().getCustomerType())
            throw new BizException("Customer Type could not be VIP for Biz One");
    }
}
