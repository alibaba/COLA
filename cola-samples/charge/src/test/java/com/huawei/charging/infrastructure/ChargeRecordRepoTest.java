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
import java.util.Date;
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
        ChargeRecord chargeRecord = new ChargeRecord(13681874561L, CallType.CALLED, 10, ChargePlanType.FAMILY, Money.of(123));
        chargeRecord.setSessionId(sessionId);
        chargeRecord.setCreateTime(new Date());
        chargeRecord.setUpdateTime(new Date());
        chargeGateway.save(chargeRecord);

        chargeRecord = chargeGateway.getBySessionId(sessionId);

        Assertions.assertEquals(chargeRecord.getSessionId(), sessionId);
    }

    @Test
    public void testSaveList(){
        List<ChargeRecord> chargeRecordList = new ArrayList<>();
        ChargeRecord chargeRecord1 = new ChargeRecord(13681874561L, CallType.CALLED, 10, ChargePlanType.FAMILY, Money.of(123));
        chargeRecord1.setSessionId(UUID.randomUUID().toString());
        ChargeRecord chargeRecord2 = new ChargeRecord(13681874561L, CallType.CALLING, 10, ChargePlanType.FAMILY, Money.of(123));
        chargeRecord2.setSessionId(UUID.randomUUID().toString());
        chargeRecordList.add(chargeRecord1);
        chargeRecordList.add(chargeRecord2);
        chargeGateway.saveAll(chargeRecordList);

        List<ChargeRecord> result = chargeGateway.findByPhoneNo(13681874561L);

        Assertions.assertEquals(result.size(), 2);
    }
}
