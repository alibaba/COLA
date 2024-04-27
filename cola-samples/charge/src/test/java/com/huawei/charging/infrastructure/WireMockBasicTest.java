package com.huawei.charging.infrastructure;


import com.alibaba.cola.test.TestsContainer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort=8080)
public class WireMockBasicTest {

    @Test
    public void testWireMockBasic() {
        // The static DSL will be automatically configured for you
        stubFor(get("/static-dsl").willReturn(ok()));
    }

    @Test
    public void testWireMockFixture(WireMockRuntimeInfo wmRuntimeInfo){
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        WireMockRegister.registerStub(wireMock, "/fixture/wiremock/stub_wire_mock_basic.json");

        System.out.println("wire mock serer port : "+wmRuntimeInfo.getHttpPort());
        TestsContainer.start();
    }

    @Test
    public void testWireMockAccount(WireMockRuntimeInfo wmRuntimeInfo){
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub_account.json");

        System.out.println("wire mock serer port : "+wmRuntimeInfo.getHttpPort());
        TestsContainer.start();
    }

    @Test
    public void testWireMockRuntime(WireMockRuntimeInfo wmRuntimeInfo){
        // Instance DSL can be obtained from the runtime info parameter
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.register(get("/v1/wiremock/basic").willReturn(ok()));

        // Info such as port numbers is also available
        System.out.println("wire mock serer port : "+wmRuntimeInfo.getHttpPort());

        TestsContainer.start();
    }
}
