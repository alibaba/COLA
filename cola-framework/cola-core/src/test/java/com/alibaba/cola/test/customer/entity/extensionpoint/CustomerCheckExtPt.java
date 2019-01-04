package com.alibaba.cola.test.customer.entity.extensionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.test.customer.entity.CustomerE;

/**
 * CustomerCheckExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:03 PM
 */
public interface CustomerCheckExtPt extends ExtensionPointI{

    //Different business check for different biz
    public void addCustomerCheck(CustomerE customerEntity);

}
