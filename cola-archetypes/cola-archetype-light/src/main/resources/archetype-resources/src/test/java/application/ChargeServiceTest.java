#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import ${package}.Application;
import ${package}.application.dto.BeginSessionRequest;
import ${package}.domain.BizException;
import ${package}.domain.gateway.AccountGateway;
import ${package}.domain.gateway.SessionGateway;
import ${package}.infrastructure.WireMockRegister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;



@SpringBootTest
@ContextConfiguration(classes = Application.class)
@WireMockTest(httpPort = 8080)
public class ChargeServiceTest {

    @Autowired
    private ChargeServiceI chargeService;

    @Autowired
    private SessionGateway sessionGateway;

    @Autowired
    private AccountGateway accountGateway;


    @Test
    public void test_session_create(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub_account.json");

        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00002";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(13681874563L);
        request.setCalledPhoneNo(15921582125L);

        chargeService.begin(request);

        Assertions.assertEquals(sessionId, sessionGateway.get(sessionId).getSessionId());
    }

    @Test
    public void test_remaining_insufficient(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMockRegister.registerStub(wmRuntimeInfo.getWireMock(), "/fixture/wiremock/stub_insufficient_account.json");

        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00003";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(13681874561L);
        request.setCalledPhoneNo(15921582125L);

        Exception exception = Assertions.assertThrows(BizException.class, () -> {
            chargeService.begin(request);
        });
        String expectedMsg = "has insufficient amount";
        String actualMsg = exception.getMessage();
        Assertions.assertTrue(actualMsg.contains(expectedMsg));
    }
}
