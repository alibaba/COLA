package com.alibaba.cola.test.customer.convertor.extensionpoint;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.entity.CustomerE;

/**
 * CustomerConvertorExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:37 AM
 */
public interface CustomerConvertorExtPt extends ConvertorI, ExtensionPointI {

    public CustomerE clientToEntity(CustomerCO customerCO, Context context);
}
