package com.alibaba.sofa.test.customer.convertor;

import com.alibaba.sofa.convertor.ConvertorI;
import com.alibaba.sofa.extension.ExtensionPointI;
import com.alibaba.sofa.test.customer.CustomerCO;
import com.alibaba.sofa.test.customer.entity.CustomerEntity;

/**
 * CustomerConvertorExtPt
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:37 AM
 */
public interface CustomerConvertorExtPt extends ConvertorI, ExtensionPointI {

    public CustomerEntity clientToEntity(CustomerCO customerCO);
}
