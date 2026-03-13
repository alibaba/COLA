package com.huawei.charging.infrastructure;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class WireMockRegister {

    public static void registerStub(WireMock wireMock, String resourcePath){
        StubMapping stubMapping = StubMapping.buildFrom(FixtureLoader.loadResource(resourcePath));
        wireMock.register(stubMapping);
    }
}
