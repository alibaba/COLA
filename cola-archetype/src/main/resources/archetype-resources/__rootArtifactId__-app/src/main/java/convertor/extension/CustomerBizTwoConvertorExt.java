#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.convertor.extension;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.extension.Extension;
import ${package}.common.BizCode;
import ${package}.convertor.CustomerConvertor;
import ${package}.convertor.extensionpoint.CustomerConvertorExtPt;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.domain.customer.valueobject.SourceType;
import ${package}.dto.clientobject.CustomerCO;
import org.springframework.beans.factory.annotation.Autowired;

@Extension(bizCode = BizCode.BIZ_TWO)
public class CustomerBizTwoConvertorExt implements CustomerConvertorExtPt{

    @Autowired
    private CustomerConvertor customerConvertor;

    @Override
    public CustomerE clientToEntity(CustomerCO customerCO, Context context) {
        CustomerE customerE = customerConvertor.clientToEntity(customerCO, context);
        customerE.setSourceType(SourceType.BIZ_TWO);
        return customerE;
    }
}