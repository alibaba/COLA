#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import ${package}.Application;
import ${package}.domain.account.Account;
import ${package}.domain.charge.Money;
import ${package}.domain.gateway.AccountGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = Application.class)
@WireMockTest(httpPort = 8080)
public class AccountGatewayTest {

    @Autowired
    AccountGateway accountGateway;

    @Test
    public void testGetAccount(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub_account.json");

        Account account = accountGateway.getAccount(15921582125L);
        System.out.println("account : " + account);

        Assertions.assertEquals(account.getPhoneNo(), 15921582125L);
        Assertions.assertEquals(account.getRemaining(), Money.of(400));
    }
}
