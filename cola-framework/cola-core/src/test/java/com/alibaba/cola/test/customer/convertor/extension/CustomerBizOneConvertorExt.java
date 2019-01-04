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
 * CustomerBizOneConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizCode = Constants.BIZ_ONE)
public class CustomerBizOneConvertorExt  implements CustomerConvertorExtPt {

    @Autowired
    private CustomerDefaultConvertorExt customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerE clientToEntity(CustomerCO customerCO, Context context){
        CustomerE customerE = customerConvertor.clientToEntity(customerCO, context);
        //In this business, AD and RFQ are regarded as different source
        if(Constants.SOURCE_AD.equals(customerCO.getSource()))
        {
            customerE.setSourceType(SourceType.AD);
        }
        if (Constants.SOURCE_RFQ.equals(customerCO.getSource())){
            customerE.setSourceType(SourceType.RFQ);
        }
        return customerE;
    }
}
