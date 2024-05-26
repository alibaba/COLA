#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.account;

import ${package}.domain.charge.Session;
import ${package}.domain.gateway.AccountGateway;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


@Component
public class AccountDomainService {

    @Resource
    private AccountGateway accountGateway;

    public void canSessionStart(Session session){
        Account callingAccount = accountGateway.getAccount(session.getCallingPhoneNo());
        Account calledAccount = accountGateway.getAccount(session.getCalledPhoneNo());
        callingAccount.checkRemaining();
        calledAccount.checkRemaining();
    }
}
