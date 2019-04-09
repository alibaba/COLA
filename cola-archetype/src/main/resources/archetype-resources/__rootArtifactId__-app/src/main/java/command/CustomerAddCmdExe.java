#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.command;


import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;

import ${package}.common.util.DomainEventPublisher;
import ${package}.convertor.CustomerConvertor;
import ${package}.domain.customer.CustomerE;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.domainevent.CustomerCreatedEvent;
import ${package}.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Command
public class CustomerAddCmdExe implements CommandExecutorI<Response, CustomerAddCmd>{

    @Autowired
    private CustomerConvertor customerConvertor;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DomainEventPublisher domainEventPublisher;


    @Override
    public Response execute(CustomerAddCmd cmd) {
        //1. biz check
        CustomerE customer = customerConvertor.clientToEntity(cmd.getCustomerCO(), cmd.getContext());
        customer.checkConfilict();

        //2. save customer
        customerRepository.save(customer);

        //3. Send domain event
        domainEventPublisher.publish(new CustomerCreatedEvent());

        return Response.buildSuccess();
    }

}
