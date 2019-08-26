package com.alibaba.cola.test.customer.convertor;

import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.test.customer.AddCustomerCmd;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.entity.CustomerEntity;

/**
 * CustomerConvertorExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:37 AM
 */
public interface CustomerConvertorExtPt extends ConvertorI, ExtensionPointI {

    public CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd);
}
