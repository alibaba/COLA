package com.alibaba.sofa.test.customer;

import com.alibaba.sofa.dto.Query;
import lombok.Data;

/**
 * GetOneCustomerQry
 *
 * @author Frank Zhang 2018-01-06 7:38 PM
 */
@Data
public class GetOneCustomerQry extends Query{
    private long customerId;
    private String companyName;
}
