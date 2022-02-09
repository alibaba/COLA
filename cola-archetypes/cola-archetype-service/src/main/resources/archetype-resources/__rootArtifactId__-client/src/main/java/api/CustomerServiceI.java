#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.CustomerListByNameQry;
import ${package}.dto.data.CustomerDTO;

public interface CustomerServiceI {

    Response addCustomer(CustomerAddCmd customerAddCmd);

    MultiResponse<CustomerDTO> listByName(CustomerListByNameQry customerListByNameQry);
}
