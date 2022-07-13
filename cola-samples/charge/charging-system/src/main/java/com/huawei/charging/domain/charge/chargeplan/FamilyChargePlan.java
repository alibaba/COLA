package com.huawei.charging.domain.charge.chargeplan;

import java.util.HashSet;
import java.util.Set;

public class FamilyChargePlan extends ChargePlan<FamilyChargePlan.FamilyMember> {

    public FamilyChargePlan() {
        this.priority = 2;
    }

    @Override
    public FamilyMember getResource() {
        return new FamilyMember();
    }

    @Override
    public ChargePlanType getType() {
        return ChargePlanType.FAMILY;
    }

    public static class FamilyMember implements Resource{
        private Set<Long> familyMembers = new HashSet<>();

        /**
         * Mock here, 真实场景，情亲号码肯定也是从外系统获取的
         */
        public FamilyMember() {
            familyMembers.add(13681874561L);
            familyMembers.add(15921582125L);
        }

        public boolean isMember(long phoneNo) {
            return familyMembers.contains(phoneNo);
        }
    }
}

