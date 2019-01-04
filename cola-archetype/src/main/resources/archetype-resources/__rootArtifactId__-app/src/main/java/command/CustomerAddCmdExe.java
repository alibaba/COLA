#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.command;


import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;

import ${package}.dto.CustomerAddCmd;
import ${package}.validator.extensionpoint.CustomerAddValidatorExtPt;
import ${package}.convertor.extensionpoint.CustomerConvertorExtPt;
import ${package}.domain.customer.entity.CustomerE;
import org.springframework.beans.factory.annotation.Autowired;


@Command
public class CustomerAddCmdExe implements CommandExecutorI<Response, CustomerAddCmd>{


    @Autowired
    private ExtensionExecutor extensionExecutor;

    @Override
    public Response execute(CustomerAddCmd cmd) {
        //1, validation
        extensionExecutor.executeVoid(CustomerAddValidatorExtPt.class, cmd.getContext(), validator -> validator.validate(cmd));

        //2, invoke domain service or directly operate domain to do business logic process
        CustomerE customerE = extensionExecutor.execute(CustomerConvertorExtPt.class, cmd.getContext(), convertor -> convertor.clientToEntity(cmd.getCustomerCO(), cmd.getContext()));
        customerE.addNewCustomer();

        //3, response
        return Response.buildSuccess();
    }

}
