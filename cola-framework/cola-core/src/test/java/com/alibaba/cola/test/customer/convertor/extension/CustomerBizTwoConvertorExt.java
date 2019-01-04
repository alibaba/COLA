package com.alibaba.cola.test.customer.convertor.extension;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.convertor.extensionpoint.CustomerConvertorExtPt;
import com.alibaba.cola.test.customer.entity.CustomerE;
import com.alibaba.cola.test.customer.entity.SourceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerBizTwoConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizCode = Constants.BIZ_TWO)
public class CustomerBizTwoConvertorExt implements CustomerConvertorExtPt {

    @Autowired
    private CustomerDefaultConvertorExt customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerE clientToEntity(CustomerCO customerCO, Context context){
        CustomerE customerE = customerConvertor.clientToEntity(customerCO, context);
        //In this business, if customers from RFQ and Advertisement are both regarded as Advertisement
        if(Constants.SOURCE_AD.equals(customerCO.getSource()) || Constants.SOURCE_RFQ.equals(customerCO.getSource()))
        {
            customerE.setSourceType(SourceType.AD);
        }
        return customerE;
    }

}
