package com.huawei.charging.application.dto;

import com.huawei.charging.domain.charge.Session;
import lombok.Data;

@Data
public class BeginSessionRequest {

    /**
     * 本次通话的UUID
     */
    private String sessionId;

    /**
     * 主叫电话号码
     */
    private long callingPhoneNo;

    /**
     * 被叫电话号码
     */
    private long calledPhoneNo;

    public Session toSession(){
        return new Session(sessionId, callingPhoneNo, calledPhoneNo);
    }

    public BeginSessionRequest() {
    }

    public BeginSessionRequest(String sessionId, long callingPhoneNo, long calledPhoneNo) {
        this.sessionId = sessionId;
        this.callingPhoneNo = callingPhoneNo;
        this.calledPhoneNo = calledPhoneNo;
    }
}
