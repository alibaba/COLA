package com.huawei.charging.application.dto;

import lombok.Data;

@Data
public class ChargeRequest {

    private String sessionId;

    /**
     * 当前通话，截止目前的累计时间
     */
    private int duration;

    public ChargeRequest() {
    }

    public ChargeRequest(String sessionId, int duration) {
        this.sessionId = sessionId;
        this.duration = duration;
    }
}
