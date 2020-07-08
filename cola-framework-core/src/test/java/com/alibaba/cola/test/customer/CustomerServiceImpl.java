package com.alibaba.cola.test.customer;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * CustomerServiceImpl
 *
 * @author Frank Zhang 2018-01-06 7:40 PM
 */
@Service
public class CustomerServiceImpl implements CustomerServiceI{

    @Resource
    private AddCustomerCmdExe addCustomerCmdExe;

    @Resource
    private GetOneCustomerQryExe getOneCustomerQryExe;


    @Override
    public Response addCustomer(AddCustomerCmd addCustomerCmd) {
        return addCustomerCmdExe.execute(addCustomerCmd);
    }

    @Override
    public SingleResponse<CustomerCO> getCustomer(GetOneCustomerQry getOneCustomerQry) {
        return getOneCustomerQryExe.execute(getOneCustomerQry);
    }
}
