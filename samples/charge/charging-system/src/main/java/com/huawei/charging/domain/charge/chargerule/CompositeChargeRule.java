package com.huawei.charging.domain.charge.chargerule;

import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.ChargeContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 为了应对套餐组合
 * 组合模式（Composite pattern)
 */
public class CompositeChargeRule {
    public List<ChargeRule> chargeRules;

    public List<ChargeRecord> doCharge(ChargeContext chargeContext){
        List<ChargeRecord> chargeRecords = new ArrayList<>();
        for(ChargeRule chargeRule : chargeRules){
            ChargeRecord chargeRecord = chargeRule.doCharge(chargeContext);
            if(chargeRecord != null){
                chargeRecords.add(chargeRecord);
            }
        }
        return chargeRecords;
    }
}
