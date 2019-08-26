#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import com.alibaba.cola.command.CommandBusI;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import ${package}.api.CustomerServiceI;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.CustomerListByNameQry;
import ${package}.dto.domainmodel.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceImpl implements CustomerServiceI {

    @Autowired
    private CommandBusI commandBus;

    @Override
    public Response addCustomer(CustomerAddCmd customerAddCmd) {
        return (Response)commandBus.send(customerAddCmd);
    }

    @Override
    public MultiResponse<Customer> listByName(CustomerListByNameQry customerListByNameQry) {
        return (MultiResponse<Customer>)commandBus.send(customerListByNameQry);
    }

}
