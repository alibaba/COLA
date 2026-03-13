package com.huawei.charging.infrastructure;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.huawei.charging.Application;
import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.gateway.AccountGateway;
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
