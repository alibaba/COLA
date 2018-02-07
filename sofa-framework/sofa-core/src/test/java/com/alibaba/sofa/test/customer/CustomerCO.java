package com.alibaba.sofa.test.customer;

import com.alibaba.sofa.dto.ClientObject;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * CustomerCO
 *
 * @author Frank Zhang 2018-01-06 7:30 PM
 */
@Data
public class CustomerCO extends ClientObject{

    @NotEmpty
    private String companyName;
    @NotEmpty
    private String source;  //advertisement, p4p, RFQ, ATM
    private CustomerType customerType; //potential, intentional, important, vip
}
