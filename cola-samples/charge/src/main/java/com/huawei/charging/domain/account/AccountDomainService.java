package com.huawei.charging.domain.account;

import com.huawei.charging.domain.charge.Session;
import com.huawei.charging.domain.gateway.AccountGateway;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
