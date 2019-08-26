package com.alibaba.cola.test.customer.convertor;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.AddCustomerCmd;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.entity.CustomerEntity;
import com.alibaba.cola.test.customer.entity.SourceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerBizTwoConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizId = Constants.BIZ_2)
public class CustomerBizTwoConvertorExt implements CustomerConvertorExtPt{

    @Autowired
    private CustomerConvertor customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd){
        CustomerEntity customerEntity = customerConvertor.clientToEntity(addCustomerCmd);
        //In this business, if customers from RFQ and Advertisement are both regarded as Advertisement
        if(Constants.SOURCE_AD.equals(addCustomerCmd.getCustomerCO().getSource()) || Constants.SOURCE_RFQ.equals(addCustomerCmd.getCustomerCO().getSource()))
        {
            customerEntity.setSourceType(SourceType.AD);
        }
        return customerEntity;
    }

}
