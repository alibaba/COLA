package com.alibaba.cola.test.customer;


import com.alibaba.cola.dto.Executor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * AddCustomerCmd
 *
 * @author Frank Zhang 2018-01-06 7:28 PM
 */
@Data
public class AddCustomerCmd extends Executor {

    @NotNull
    @Valid
    private CustomerCO customerCO;
}
