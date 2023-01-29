package com.huawei.charging;

import com.huawei.charging.domain.charge.CallType;
import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import com.huawei.charging.domain.gateway.ChargeGateway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChargeRecordRepoTest {
    @Resource
    private ChargeGateway chargeGateway;

    private String sessionId;

    @Before
    public void setup(){
        sessionId = UUID.randomUUID().toString();
    }

    @Test
    public void testSave(){
        List<ChargeRecord> chargeRecordList = new ArrayList<>();
        ChargeRecord chargeRecord = new ChargeRecord(13681874561L, CallType.CALLED, 10, ChargePlanType.FAMILY, Money.of(123));
        chargeRecord.setSessionId(sessionId);
        chargeRecordList.add(chargeRecord);
        chargeGateway.saveAll(chargeRecordList);
    }

    @Test
    public void testFindBySessionId(){
        testSave();
        List<ChargeRecord> chargeRecordList = chargeGateway.findBySessionId(sessionId);
        System.out.println(chargeRecordList.get(0));
        Assert.assertEquals(chargeRecordList.size(), 1);
        chargeGateway.delete(chargeRecordList.get(0));
    }
}
