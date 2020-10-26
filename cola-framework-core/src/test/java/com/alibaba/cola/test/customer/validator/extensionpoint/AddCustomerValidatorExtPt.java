package com.alibaba.cola.test.customer.validator.extensionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.test.customer.AddCustomerCmd;
import com.alibaba.cola.test.customer.entity.CustomerEntity;

/**
 * AddCustomerValidatorExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 1:27 AM
 */
public interface AddCustomerValidatorExtPt extends ExtensionPointI {

    public void validate(AddCustomerCmd addCustomerCmd);
}
