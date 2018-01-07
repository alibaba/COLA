package com.alibaba.sofa.test.customer;

import com.alibaba.sofa.dto.Response;
import com.alibaba.sofa.dto.SingleResponse;

/**
 * CustomerServiceI
 *
 * @author Frank Zhang 2018-01-06 7:24 PM
 */
public interface CustomerServiceI {
    public Response addCustomer(AddCustomerCmd addCustomerCmd);
    public SingleResponse<CustomerCO> getCustomer(GetOneCustomerQry getOneCustomerQry);
}
