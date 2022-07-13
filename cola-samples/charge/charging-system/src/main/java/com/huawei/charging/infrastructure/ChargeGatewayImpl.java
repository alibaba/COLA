package com.huawei.charging.infrastructure;

import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.gateway.ChargeGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChargeGatewayImpl implements ChargeGateway {
    private final List<ChargeRecord>  records =  new ArrayList<>();

    @Override
    public void save(List<ChargeRecord> records) {
        records.addAll(records);
        // dummy save
    }
}
