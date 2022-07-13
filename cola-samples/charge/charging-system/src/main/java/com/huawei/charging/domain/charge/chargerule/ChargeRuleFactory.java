package com.huawei.charging.domain.charge.chargerule;

import com.huawei.charging.domain.ApplicationContextHelper;
import com.huawei.charging.domain.charge.chargeplan.ChargePlan;
import com.huawei.charging.domain.charge.chargeplan.ChargePlanType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChargeRuleFactory {
    public static CompositeChargeRule get(List<ChargePlan> chargePlanList) {
        //按套餐的优先级进行排序
        Collections.sort(chargePlanList);

        List<ChargeRule> chargeRules = new ArrayList<>();
        for (ChargePlan chargePlan : chargePlanList) {
            ChargeRule chargeRule;
            if (chargePlan.getType() == ChargePlanType.FAMILY) {
                chargeRule = ApplicationContextHelper.getBean(FamilyChargeRule.class);
            } else if (chargePlan.getType() == ChargePlanType.FIXED_TIME) {
                chargeRule = ApplicationContextHelper.getBean(FixedTimeChargeRule.class);
            } else {
                chargeRule = ApplicationContextHelper.getBean(BasicChargeRule.class);
            }
            chargeRule.belongsTo(chargePlan);
            chargeRules.add(chargeRule);
        }
        CompositeChargeRule compositeChargeRule = new CompositeChargeRule();
        compositeChargeRule.chargeRules = chargeRules;
        return compositeChargeRule;
    }
}
