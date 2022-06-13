package com.huawei.charging.domain.charge;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Session {
    private String sessionId;

    /**
     * 主叫电话号码
     */
    private long callingPhoneNo;

    /**
     * 被叫电话号码
     */
    private long calledPhoneNo;

    /**
     * 当前通话已扣费的时长
     *
     */
    private int chargedDuration;

    /**
     * 当前通话产生的Charge记录
     */
    private List<ChargeRecord> chargeRecordList = new ArrayList<>();

    public Session(String sessionId, long callingPhoneNo, long calledPhoneNo) {
        this.sessionId = sessionId;
        this.callingPhoneNo = callingPhoneNo;
        this.calledPhoneNo = calledPhoneNo;
    }

}
