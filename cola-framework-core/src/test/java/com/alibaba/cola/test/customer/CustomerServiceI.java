package com.alibaba.cola.test.customer;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;

/**
 * CustomerServiceI
 *
 * @author Frank Zhang 2018-01-06 7:24 PM
 */
public interface CustomerServiceI {
    public Response addCustomer(AddCustomerCmd addCustomerCmd);
    public SingleResponse<CustomerCO> getCustomer(GetOneCustomerQry getOneCustomerQry);
}
