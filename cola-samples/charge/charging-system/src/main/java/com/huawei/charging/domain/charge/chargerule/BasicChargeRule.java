package com.huawei.charging.domain.charge.chargerule;

import com.huawei.charging.domain.charge.*;
import com.huawei.charging.domain.charge.chargeplan.BasicChargePlan;
import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BasicChargeRule extends AbstractChargeRule{
    @Override
    public ChargeRecord doCharge(ChargeContext ctx) {
        if(!ctx.needCharge()){
            log.debug("No need charge for : "+ctx);
            return null;
        }
        BasicChargePlan basicChargePlan = (BasicChargePlan)chargePlan;
        BasicChargePlan.BasicChargeFee chargeFee = basicChargePlan.getResource();
        Money cost;
        int duration = ctx.durationToCharge;
        if (ctx.callType == CallType.CALLING) {
            cost = Money.of(duration * chargeFee.CALLING_PRICE);
        } else {
            cost = Money.of(duration * chargeFee.CALLED_PRICE);
        }
        ChargeRecord chargeRecord = new ChargeRecord(ctx.phoneNo, ctx.callType, duration, ChargePlanType.BASIC, cost);

        //在账号上扣减费用
        ctx.account.getRemaining().minus(cost);
        ctx.setDurationToCharge(0);
        return chargeRecord;
    }
}
