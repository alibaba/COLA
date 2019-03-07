package com.alibaba.cola.test.customer;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;

/**
 * AddCustomerErrorCmdExe
 *
 * @author Frank Zhang
 * @date 2019-02-28 6:52 PM
 */
public class AddCustomerErrorCmdExe implements CommandExecutorI<Response, AddCustomerCmd> {

    @Override
    public Response execute(AddCustomerCmd cmd) {
        return null;
    }
}
