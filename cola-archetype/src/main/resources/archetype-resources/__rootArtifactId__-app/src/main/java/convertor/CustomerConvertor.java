#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.convertor;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.convertor.ConvertorI;
import ${package}.dto.clientobject.CustomerCO;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.tunnel.database.dataobject.CustomerDO;
import org.springframework.stereotype.Component;
/**
 * General CustomerConvertor, the difference is implemented by Extension mechanism
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Component
public class CustomerConvertor implements ConvertorI {

  public CustomerE clientToEntity(CustomerCO customerCO, Context context){
      CustomerE customerE = DomainFactory.create(CustomerE.class);
      customerE.setCompanyName(customerCO.getCompanyName());
      customerE.setContext(context);
      return customerE;
  }

  public CustomerCO dataToClient(CustomerDO dataObject) {
      CustomerCO customerCO = new CustomerCO();
      customerCO.setCustomerName(dataObject.getCompanyName());
      customerCO.setCustomerType(dataObject.getCompanyType());
      customerCO.setMemberId(dataObject.getMemberId());
      customerCO.setCustomerId(dataObject.getCustomerId());
      return customerCO;
  }
}
