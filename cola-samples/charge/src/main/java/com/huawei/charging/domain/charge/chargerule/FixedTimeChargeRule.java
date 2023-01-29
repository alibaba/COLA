package com.huawei.charging.domain.charge.chargerule;

import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.ChargeContext;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import com.huawei.charging.domain.charge.chargeplan.FixedTimeChangePlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FixedTimeChargeRule extends AbstractChargeRule {
    @Override
    public ChargeRecord doCharge(ChargeContext ctx) {
        if(!ctx.needCharge()){
            log.debug("No need charge for : "+ctx);
            return null;
        }
        FixedTimeChangePlan fixedTimeChangePlan = (FixedTimeChangePlan) chargePlan;
        FixedTimeChangePlan.FreeCallTime freeCallTime = fixedTimeChangePlan.getResource();
        if (ctx.isCalling() && freeCallTime.isCallingTimeRemaining()) {
            int leftDuration = freeCallTime.chargeFreeCallingTime(ctx.durationToCharge);
            log.debug("Calling Left Duration after FixedTimeCharge : " + leftDuration);
            ChargeRecord chargeRecord = new ChargeRecord(ctx.phoneNo, ctx.callType, ctx.durationToCharge - leftDuration, ChargePlanType.FIXED_TIME, Money.of(0));
            ctx.setDurationToCharge(leftDuration);
            return chargeRecord;
        }
        if (ctx.isCalled() && freeCallTime.isCalledTimeRemaining()) {
            int leftDuration = freeCallTime.chargeFreeCalledTime(ctx.durationToCharge);
            log.debug("Called Left Duration after FixedTimeCharge : " + leftDuration);
            ChargeRecord chargeRecord = new ChargeRecord(ctx.phoneNo, ctx.callType, ctx.durationToCharge - leftDuration, ChargePlanType.FIXED_TIME, Money.of(0));
            ctx.setDurationToCharge(leftDuration);
            return chargeRecord;
        }
        return null;
    }
}
