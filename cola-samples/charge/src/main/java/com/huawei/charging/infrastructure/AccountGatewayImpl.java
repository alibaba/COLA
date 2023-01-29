package com.huawei.charging.infrastructure;

import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.gateway.AccountGateway;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountGatewayImpl implements AccountGateway {
    private static final String URL_TO_GET_ACCOUNT = "http://internel.xxx.com/api/account/{account}";
    private static final String URL_TO_SYNC_ACCOUNT = "http://internel.xxx.com/api/account/{account}/sync";

    private Map<Long, Account> accountMap = new HashMap<>();

    @Override
    public Account getAccount(long phoneNo) {
        if(accountMap.get(phoneNo) == null){
            accountMap.put(phoneNo, Account.valueOf(phoneNo, Money.of(200)));
        }
        return accountMap.get(phoneNo);
    }

    @Override
    public void sync(long phoneNo, List<ChargeRecord> records) {
        // 更新账户系统
    }
}
