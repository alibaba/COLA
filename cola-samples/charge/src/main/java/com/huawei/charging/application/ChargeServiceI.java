package com.huawei.charging.application;

import com.huawei.charging.application.dto.*;

public interface ChargeServiceI {
    Response begin(BeginSessionRequest request);

    Response charge(ChargeRequest request);

    Response end(EndSessionRequest request);

    MultiResponse<ChargeRecordDto> listChargeRecords(String sessionId);
}
