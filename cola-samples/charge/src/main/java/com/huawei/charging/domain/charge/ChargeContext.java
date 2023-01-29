package com.huawei.charging.domain.charge;

import com.huawei.charging.domain.account.Account;
import lombok.Data;

@Data
public class ChargeContext {

    /**
     * 本次通话的Session
     */
    public Session session;

    /**
     * 呼叫类型
     */
    public CallType callType;
    /**
     * 账号号码
     */
    public long phoneNo;

    /**
     * 通话另一端号码
     */
    public long otherSidePhoneNo;

    /**
     * 当前需要被扣费的时长
     */
    public int durationToCharge;

    /**
     * 被Charge的账号
     */
    public Account account;

    public ChargeContext(CallType callType, long phoneNo, long otherSidePhoneNo, int durationToCharge) {
        this.callType = callType;
        this.phoneNo = phoneNo;
        this.otherSidePhoneNo = otherSidePhoneNo;
        this.durationToCharge = durationToCharge;
    }

    public boolean needCharge(){
        return durationToCharge >0;
    }

    public boolean isCalling(){
        return CallType.CALLING ==  this.callType;
    }

    public boolean isCalled(){
        return CallType.CALLED == this.callType;
    }

    @Override
    public String toString() {
        return "ChargeContext{" +
                "callType=" + callType +
                ", phoneNo=" + phoneNo +
                ", otherSidePhoneNo=" + otherSidePhoneNo +
                ", durationToCharge=" + durationToCharge +
                ", account=" + account +
                '}';
    }
}
