package com.alibaba.sofa.test.customer.convertor;

import com.alibaba.sofa.extension.Extension;
import com.alibaba.sofa.test.customer.Constants;
import com.alibaba.sofa.test.customer.CustomerCO;
import com.alibaba.sofa.test.customer.convertor.CustomerConvertorExtPt;
import com.alibaba.sofa.test.customer.entity.CustomerEntity;
import com.alibaba.sofa.test.customer.entity.SourceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerBizTwoConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizCode = Constants.BIZ_2)
public class CustomerBizTwoConvertorExt implements CustomerConvertorExtPt{

    @Autowired
    private CustomerConvertor customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerEntity convert(CustomerCO customerCO){
        CustomerEntity customerEntity = customerConvertor.convert(customerCO);
        //In this business, if customers from RFQ and Advertisement are both regarded as Advertisement
        if(Constants.SOURCE_AD.equals(customerCO.getSource()) || Constants.SOURCE_RFQ.equals(customerCO.getSource()))
        {
            customerEntity.setSourceType(SourceType.AD);
        }
        return customerEntity;
    }

}
