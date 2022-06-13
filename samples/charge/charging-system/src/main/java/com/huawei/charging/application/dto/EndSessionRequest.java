package com.huawei.charging.application.dto;

import lombok.Data;

@Data
public class EndSessionRequest {
    private String sessionId;

    /**
     * 当前通话，截止目前的累计时间
     */
    private int duration;

    public ChargeRequest toChargeRequest() {
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setSessionId(sessionId);
        chargeRequest.setDuration(duration);
        return chargeRequest;
    }
}
