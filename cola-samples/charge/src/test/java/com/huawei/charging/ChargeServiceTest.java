package com.huawei.charging;

import com.huawei.charging.application.ChargeServiceI;
import com.huawei.charging.application.dto.ChargeRequest;
import com.huawei.charging.application.dto.EndSessionRequest;
import com.huawei.charging.application.dto.BeginSessionRequest;
import com.huawei.charging.domain.BizException;
import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.gateway.AccountGateway;
import com.huawei.charging.domain.gateway.SessionGateway;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class ChargeServiceTest {

    @Resource
    private ChargeServiceI chargeService;

    @Resource
    private SessionGateway sessionGateway;

    @Resource
    private AccountGateway accountGateway;

    @Test
    public void test_session_create(){
        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00002";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(13681874563L);
        request.setCalledPhoneNo(15921582125L);

        chargeService.begin(request);

        Assert.assertEquals(sessionId, sessionGateway.get(sessionId).getSessionId());
    }

    @Test
    public void test_remaining_insufficient(){
        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00003";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(13681874561L);
        request.setCalledPhoneNo(15921582125L);

        //mock insufficient
        Account account = accountGateway.getAccount(13681874561L);
        account.getRemaining().minus(Money.of(200));

        try {
            chargeService.begin(request);
            Assert.fail("BizException not thrown");
        }
        catch (BizException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_normal_charge(){
        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00001";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(13681874533L);
        request.setCalledPhoneNo(15921582155L);

        chargeService.begin(request);

        ChargeRequest chargeRequest =  new ChargeRequest();
        chargeRequest.setSessionId(sessionId);
        chargeRequest.setDuration(10);

        chargeService.charge(chargeRequest);

        Account callingAccount = accountGateway.getAccount(13681874533L);
        Account calledAccount = accountGateway.getAccount(15921582155L);
        Assert.assertEquals(Money.of(150), callingAccount.getRemaining());
        Assert.assertEquals(Money.of(160), calledAccount.getRemaining());
    }

    @Test
    public void test_session_end(){
        BeginSessionRequest request = new BeginSessionRequest();
        String sessionId = "00004";
        request.setSessionId(sessionId);
        request.setCallingPhoneNo(14681874533L);
        request.setCalledPhoneNo(14921582155L);

        chargeService.begin(request);

        EndSessionRequest endReq =  new EndSessionRequest();
        endReq.setSessionId("00004");
        endReq.setDuration(20);

        chargeService.end(endReq);

        Account callingAccount = accountGateway.getAccount(14681874533L);
        Account calledAccount = accountGateway.getAccount(14921582155L);
        Assert.assertEquals(Money.of(100), callingAccount.getRemaining());
        Assert.assertEquals(Money.of(120), calledAccount.getRemaining());
        Assert.assertEquals(null, sessionGateway.get("00004"));
    }
}
