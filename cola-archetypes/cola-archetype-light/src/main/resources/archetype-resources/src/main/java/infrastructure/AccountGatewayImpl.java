#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure;

import ${package}.domain.account.Account;
import ${package}.domain.charge.ChargeRecord;
import ${package}.domain.charge.Money;
import ${package}.domain.gateway.AccountGateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AccountGatewayImpl implements AccountGateway {
    private static final String GET_ACCOUNT_PATH = "/v1/api/account/{account}";
    private static final String SYNC_ACCOUNT_PATH = "/v1/api/account/account/{account}/sync";

    private Map<Long, Account> accountMap = new HashMap<>();

    @Autowired
    private RestClient restClient;

    @Override
    public Account getAccount(long phoneNo) {
        Account account = restClient.get()
                .uri(GET_ACCOUNT_PATH, phoneNo)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Account.class);

        return account;
    }

    @Override
    public void sync(long phoneNo, List<ChargeRecord> records) {
        // 更新账户系统
        log.info("sync account info, to be implemented");
    }
}
