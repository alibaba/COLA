package com.huawei.charging.infrastructure;

import com.huawei.charging.domain.charge.CallType;
import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import com.huawei.charging.domain.gateway.ChargeGateway;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ChargeRecordRepoTest {
    @Resource
    private ChargeGateway chargeGateway;

    private String sessionId;

    @BeforeEach
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
        Assertions.assertEquals(chargeRecordList.size(), 1);
        chargeGateway.delete(chargeRecordList.get(0));
    }
}
