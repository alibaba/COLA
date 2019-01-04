#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api;

import ${package}.dto.CustomerAddCmd;
import ${package}.dto.CustomerFindByCriteriaQry;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import ${package}.dto.clientobject.CustomerCO;

public interface CustomerServiceI {

    public Response addCustomer(CustomerAddCmd customerAddCmd);
        
    public MultiResponse<CustomerCO> findByCriteria(CustomerFindByCriteriaQry CustomerFindByCriteriaQry);
}
