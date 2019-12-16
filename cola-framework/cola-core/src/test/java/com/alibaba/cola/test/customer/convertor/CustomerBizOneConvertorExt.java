package com.alibaba.cola.test.customer.convertor;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.test.customer.AddCustomerCmd;
import com.alibaba.cola.test.customer.Constants;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.entity.CustomerEntity;
import com.alibaba.cola.test.customer.entity.SourceType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerBizOneConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizId = Constants.BIZ_1)
public class CustomerBizOneConvertorExt  implements CustomerConvertorExtPt{

    @Autowired
    private CustomerConvertor customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerEntity clientToEntity(AddCustomerCmd addCustomerCmd){
        CustomerEntity customerEntity = customerConvertor.clientToEntity(addCustomerCmd);
        CustomerCO customerCO=addCustomerCmd.getCustomerCO();
        //In this business, AD and RFQ are regarded as different source
        if(Constants.SOURCE_AD.equals(customerCO.getSource()))
        {
            customerEntity.setSourceType(SourceType.AD);
        }
        if (Constants.SOURCE_RFQ.equals(customerCO.getSource())){
            customerEntity.setSourceType(SourceType.RFQ);
        }
        return customerEntity;
    }
}
