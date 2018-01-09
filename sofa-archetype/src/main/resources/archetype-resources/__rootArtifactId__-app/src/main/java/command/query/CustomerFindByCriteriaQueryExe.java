#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.command.query;

import ${package}.domain.customer.convertor.CustomerConvertor;
import ${package}.tunnel.dataobject.CustomerDO;
import ${package}.tunnel.datatunnel.CustomerTunnelI;
import ${package}.dto.CustomerFindByCriteriaQuery;
import ${package}.dto.clientobject.CustomerCO;
import com.alibaba.sofa.command.Command;
import com.alibaba.sofa.command.QueryExecutorI;
import com.alibaba.sofa.dto.MultiResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Command
public class CustomerFindByCriteriaQueryExe implements QueryExecutorI<MultiResponse<CustomerCO>, CustomerFindByCriteriaQuery> {

    @Autowired
    CustomerTunnelI customerDBTunnel;
    
    @Autowired
    CustomerConvertor customerConvertor;
    
    @Override
    public MultiResponse<CustomerCO> execute(CustomerFindByCriteriaQuery cmd) {
        CustomerDO customerDO = customerDBTunnel.get("123");
        List<CustomerCO> customerCos = new ArrayList<>();
        customerCos.add(customerConvertor.dataToClient(customerDO));
        return MultiResponse.of(customerCos, customerCos.size());
    }
    

    public CustomerDO get(String id) {
        CustomerDO customerDO = customerDBTunnel.get("123");
        return customerDO;
    }
}
