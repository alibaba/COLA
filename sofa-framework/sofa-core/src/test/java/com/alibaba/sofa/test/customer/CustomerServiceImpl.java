package com.alibaba.sofa.test.customer;

import com.alibaba.sofa.command.CommandBusI;
import com.alibaba.sofa.dto.Response;
import com.alibaba.sofa.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CustomerServiceImpl
 *
 * @author Frank Zhang 2018-01-06 7:40 PM
 */
@Service
public class CustomerServiceImpl implements CustomerServiceI{

    @Autowired
    private CommandBusI commandBus;

    @Override
    public Response addCustomer(AddCustomerCmd addCustomerCmd) {
        return (Response)commandBus.send(addCustomerCmd);
    }

    @Override
    public SingleResponse<CustomerCO> getCustomer(GetOneCustomerQry getOneCustomerQry) {
        return null;
    }
}
