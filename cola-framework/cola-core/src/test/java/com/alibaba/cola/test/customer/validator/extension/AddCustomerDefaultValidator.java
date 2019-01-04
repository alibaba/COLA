package com.alibaba.cola.test.customer.validator.extension;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.validator.extensionpoint.AddCustomerValidatorExtPt;

/**
 * AddCustomerDefaultValidator
 *
 * @author Frank Zhang
 * @date 2019-01-03 11:14 AM
 */
@Extension
public class AddCustomerDefaultValidator implements AddCustomerValidatorExtPt {

    @Override
    public void validate(Object candidate) {
        //do nothing
    }
}
