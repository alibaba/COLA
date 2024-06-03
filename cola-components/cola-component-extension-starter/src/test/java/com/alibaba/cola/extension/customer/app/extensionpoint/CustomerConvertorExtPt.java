package com.alibaba.cola.extension.customer.app.extensionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.extension.customer.client.AddCustomerCmd;
import com.alibaba.cola.extension.customer.domain.CustomerEntity;

/**
 * CustomerConvertorExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:37 AM
 */
public interface CustomerConvertorExtPt extends ExtensionPointI {

    public CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd);
}
