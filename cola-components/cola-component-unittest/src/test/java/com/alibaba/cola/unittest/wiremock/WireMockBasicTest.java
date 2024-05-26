package com.alibaba.cola.unittest.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8080)
@Slf4j
public class WireMockBasicTest {

    @Autowired
    protected WebTestClient webClient;

    @Test
    public void testWireMockBasic() {
        // The static DSL will be automatically configured for you
        stubFor(get("/static-dsl").willReturn(ok()));

        webClient.get()
                .uri("http://localhost:8080/static-dsl")
                .exchange()
                .expectStatus()
                .isEqualTo(200);
    }

    @Test
    public void testWireMockStub(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub-wire-mock-basic.json");

        webClient.get()
                .uri("http://localhost:8080/v1/wiremock/basic")
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON);

        System.out.println("wire mock serer port : " + wmRuntimeInfo.getHttpPort());
    }

    @Test
    public void testWireMockAccount(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub-account.json");

        long phoneNo = 123456789;

        webClient.get()
                .uri("http://localhost:8080/v1/api/account/"+phoneNo)
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .returnResult(Account.class)
                .getResponseBody()
                .map(account -> {
                    log.info(account.toString());
                    Assertions.assertEquals("frank", account.getName());
                    Assertions.assertEquals(phoneNo, account.getPhoneNo());
                    return account;
                })
                .subscribe();

        log.info("wire mock serer port : " + wmRuntimeInfo.getHttpPort());
    }

}

