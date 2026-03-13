package com.alibaba.cola.extension.customer.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.extension.customer.client.AddCustomerCmd;
import com.alibaba.cola.extension.customer.client.CustomerDTO;
import com.alibaba.cola.extension.customer.client.CustomerServiceI;
import com.alibaba.cola.extension.customer.client.GetOneCustomerQry;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * CustomerServiceImpl
 *
 * @author Frank Zhang 2018-01-06 7:40 PM
 */
@Service
public class CustomerServiceImpl implements CustomerServiceI {

    @Resource
    private AddCustomerCmdExe addCustomerCmdExe;

    @Resource
    private GetOneCustomerQryExe getOneCustomerQryExe;


    @Override
    public Response addCustomer(AddCustomerCmd addCustomerCmd) {
        return addCustomerCmdExe.execute(addCustomerCmd);
    }

    @Override
    public SingleResponse<CustomerDTO> getCustomer(GetOneCustomerQry getOneCustomerQry) {
        return getOneCustomerQryExe.execute(getOneCustomerQry);
    }
}
