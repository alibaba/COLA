package com.huawei.charging.application;

import com.huawei.charging.application.dto.ChargeRequest;
import com.huawei.charging.application.dto.EndSessionRequest;
import com.huawei.charging.application.dto.InitSessionRequest;
import com.huawei.charging.application.dto.Response;

public interface ChargeServiceI {
    public Response begin(InitSessionRequest request);

    public Response charge(ChargeRequest request);

    Response end(EndSessionRequest request);
}
