#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.convertor;

import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import ${package}.context.DemoContent;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.tunnel.database.dataobject.CustomerDO;
import org.springframework.stereotype.Component;

/**
 * CustomerConvertor
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Component
public class CustomerDomainConvertor implements ConvertorI {
    private Logger logger = LoggerFactory.getLogger(CustomerDomainConvertor.class);

    public CustomerDO entityToData(CustomerE customerE){
        CustomerDO customerDO = new CustomerDO();
        customerDO.setCompanyName(customerE.getCompanyName());
        DemoContent demoContent = (DemoContent) customerE.getContext().getContent();
        logger.debug("The userId from context is:"+demoContent.getUserId());
        customerDO.setCreator(demoContent.getUserId());
        return customerDO;
    }
}
