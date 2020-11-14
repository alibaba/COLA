package com.alibaba.cola.extension.test.customer.client;


import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import lombok.Data;

/**
 * AddCustomerCmd
 *
 * @author Frank Zhang 2018-01-06 7:28 PM
 */
@Data
public class AddCustomerCmd extends Command {

    private CustomerDTO customerDTO;

    private String biz;

    private BizScenario bizScenario;
}
