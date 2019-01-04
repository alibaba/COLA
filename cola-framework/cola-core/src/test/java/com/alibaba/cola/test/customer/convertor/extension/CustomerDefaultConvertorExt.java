package com.alibaba.cola.test.customer.convertor.extension;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.convertor.extensionpoint.CustomerConvertorExtPt;
import com.alibaba.cola.test.customer.entity.CustomerE;

/**
 * CustomerDefaultConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Extension
public class CustomerDefaultConvertorExt implements CustomerConvertorExtPt {

    public CustomerE clientToEntity(CustomerCO customerCO, Context context){
        CustomerE customerE = DomainFactory.create(CustomerE.class);
        customerE.setCompanyName(customerCO.getCompanyName());
        customerE.setCustomerType(customerCO.getCustomerType());
        customerE.setContext(context);
        return customerE;
    }
}
