package com.huawei.charging.domain.gateway;

import com.huawei.charging.domain.charge.ChargeRecord;

import java.util.List;

public interface ChargeGateway {

    public void save(List<ChargeRecord> records);
}
