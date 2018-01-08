#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.convertor.extension;

import ${package}.config.AppConstants;
import ${package}.config.BizCode;
import ${package}.dataobject.CustomerDO;
import ${package}.domain.customer.convertor.CustomerConvertor;
import ${package}.domain.customer.convertor.extensionpoint.CustomerConvertorExtPt;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.domain.customer.valueobject.SourceType;
import ${package}.dto.clientobject.CustomerCO;
import com.alibaba.sofa.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CustomerCGSConvertorExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:05 AM
 */
@Extension(bizCode = BizCode.CGS)
public class CustomerCGSConvertorExt implements CustomerConvertorExtPt {

    @Autowired
    private CustomerConvertor customerConvertor;//Composite basic convertor to do basic conversion

    @Override
    public CustomerE clientToEntity(CustomerCO customerCO){
        CustomerE customerEntity = customerConvertor.clientToEntity(customerCO);
        //In this business, if customers from RFQ and Advertisement are both regarded as Advertisement
        if(AppConstants.SOURCE_AD.equals(customerCO.getSource()) || AppConstants.SOURCE_RFQ.equals(customerCO.getSource()))
        {
            customerEntity.setSourceType(SourceType.AD);
        }
        return customerEntity;
    }

    public CustomerCO dataToClient(CustomerDO customerDO){
        return customerConvertor.dataToClient(customerDO);
    }
}
