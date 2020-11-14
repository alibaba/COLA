package com.alibaba.cola.extension.test.customer.client;

import com.alibaba.cola.dto.Query;

/**
 * GetOneCustomerQry
 *
 * @author Frank Zhang 2018-01-06 7:38 PM
 */
public class GetOneCustomerQry extends Query{
    private long customerId;
    private String companyName;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
