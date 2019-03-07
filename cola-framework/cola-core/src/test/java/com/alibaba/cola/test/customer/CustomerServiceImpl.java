package com.alibaba.cola.test.customer;

import com.alibaba.cola.command.CommandBusI;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
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
    public Response addCustomer2(AddCustomerCmd addCustomerCmd) {
        return (Response)commandBus.send(addCustomerCmd, AddCustomerCmdExe.class);
    }

    @Override
    public Response addCustomerParaError(AddCustomerCmd addCustomerCmd) {
        return (Response)commandBus.send(addCustomerCmd, AddCustomerErrorCmdExe.class);
    }


    @Override
    public SingleResponse<CustomerCO> getCustomer(GetOneCustomerQry getOneCustomerQry) {
        return null;
    }
}
