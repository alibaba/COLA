package com.huawei.charging.domain.charge;

import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import lombok.Data;

@Data
public class ChargeRecord {

    private long phoneNo;

    /**
     * 呼叫类型
     */
    private CallType callType;

    /**
     * 计费记录所对应的呼叫时长
     */
    private int chargeDuration;

    /**
     * 所属计费套餐
     */
    private ChargePlanType chargePlanType;

    private Money cost;

    public ChargeRecord(long phoneNo, CallType callType, int chargeDuration, ChargePlanType chargePlanType, Money cost) {
        this.phoneNo = phoneNo;
        this.callType = callType;
        this.chargeDuration = chargeDuration;
        this.chargePlanType = chargePlanType;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "phoneNo=" + phoneNo +
                ", callType=" + callType +
                ", chargeDuration=" + chargeDuration +
                ", chargePlanType=" + chargePlanType +
                ", cost=" + cost +
                '}';
    }
}
