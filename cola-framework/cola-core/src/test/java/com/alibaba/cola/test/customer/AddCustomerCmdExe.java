package com.alibaba.cola.test.customer;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.cola.test.customer.convertor.extensionpoint.CustomerConvertorExtPt;
import com.alibaba.cola.test.customer.entity.CustomerE;
import com.alibaba.cola.test.customer.validator.extensionpoint.AddCustomerValidatorExtPt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AddCustomerCmdExe
 *
 * @author Frank Zhang 2018-01-06 7:48 PM
 */
@Command
public class AddCustomerCmdExe implements CommandExecutorI<Response, AddCustomerCmd> {

    private Logger logger = LoggerFactory.getLogger(AddCustomerCmd.class);

    @Autowired
    private ExtensionExecutor extensionExecutor;


    @Override
    public Response execute(AddCustomerCmd cmd) {
        logger.info("Start processing command:" + cmd);
        //Do validation
        extensionExecutor.executeVoid(AddCustomerValidatorExtPt.class, cmd.getContext(), validator -> validator.validate(cmd));

        //Convert CO to EntityObject
        CustomerE customerE = extensionExecutor.execute(CustomerConvertorExtPt.class, cmd.getContext(), convertor -> convertor.clientToEntity(cmd.getCustomerCO(), cmd.getContext()));

        //Call Domain EntityObject for business logic processing
        logger.info("Call Domain EntityObject for business logic processing..."+customerE);
        customerE.addNewCustomer();

        logger.info("End processing command:" + cmd);
        return Response.buildSuccess();
    }
}
