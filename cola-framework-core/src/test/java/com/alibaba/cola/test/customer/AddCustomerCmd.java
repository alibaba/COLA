package com.alibaba.cola.test.customer;


import com.alibaba.cola.dto.Command;

/**
 * AddCustomerCmd
 *
 * @author Frank Zhang 2018-01-06 7:28 PM
 */
public class AddCustomerCmd extends Command {


    public CustomerCO getCustomerCO() {
        return customerCO;
    }

    public void setCustomerCO(CustomerCO customerCO) {
        this.customerCO = customerCO;
    }

    private CustomerCO customerCO;


}
