package com.huawei.charging.domain.charge;

import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "charge_record")
@Data
public class ChargeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String sessionId;

    private long phoneNo;

    /**
     * 呼叫类型
     */
    @Enumerated(EnumType.STRING)
    private CallType callType;

    /**
     * 计费记录所对应的呼叫时长
     */
    private int chargeDuration;

    /**
     * 所属计费套餐
     */
    @Enumerated(EnumType.STRING)
    private ChargePlanType chargePlanType;

    private Money cost;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    public Date updateTime;

    public ChargeRecord() {
    }

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
