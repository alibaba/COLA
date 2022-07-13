package com.huawei.charging.domain.charge.chargerule;

import com.huawei.charging.domain.charge.chargeplan.ChargePlan;

public abstract class AbstractChargeRule implements ChargeRule{
    protected ChargePlan chargePlan;

    @Override
    public void belongsTo(ChargePlan chargePlan){
        this.chargePlan = chargePlan;
    }


}
