package com.huawei.charging.domain;

import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.CallType;
import com.huawei.charging.domain.charge.ChargeContext;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.charge.chargeplan.BasicChargePlan;
import com.huawei.charging.domain.charge.chargeplan.ChargePlan;
import com.huawei.charging.domain.charge.chargeplan.FamilyChargePlan;
import com.huawei.charging.domain.charge.chargeplan.FixedTimeChangePlan;
import com.huawei.charging.domain.charge.chargerule.BasicChargeRule;
import com.huawei.charging.domain.charge.chargerule.FamilyChargeRule;
import com.huawei.charging.domain.charge.chargerule.FixedTimeChargeRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ChargeRecordRuleTest {

    @Test
    public void test_basic_charge_rule(){
        //prepare
        ChargePlan chargePlan = new BasicChargePlan();
        Account account = new Account(13681874561L, Money.of(200), Collections.singletonList(chargePlan));
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 20);
        ctx.account = account;
        System.out.println("Account before charge: "+ account);

        //do
        BasicChargeRule basicChargeRule = new BasicChargeRule();
        basicChargeRule.belongsTo(chargePlan);
        basicChargeRule.doCharge(ctx);

        //check
        System.out.println("Account after charge: "+ account);
        Assertions.assertEquals( Money.of(100), ctx.account.getRemaining());
        Assertions.assertEquals( 0, ctx.getDurationToCharge());
    }

    @Test
    public void test_family_charge_rule(){
        //prepare
        FamilyChargePlan chargePlan = new FamilyChargePlan();
        Account account = new Account(13681874561L, Money.of(200), Collections.singletonList(chargePlan));
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 20);
        ctx.account = account;
        System.out.println("Account before charge: "+ account);

        //do
        FamilyChargeRule familyChargeRule = new FamilyChargeRule();
        familyChargeRule.belongsTo(chargePlan);
        familyChargeRule.doCharge(ctx);

        //check
        System.out.println("Account after charge: "+ account);
        Assertions.assertEquals( Money.of(200), ctx.account.getRemaining());
        Assertions.assertEquals( 0, ctx.getDurationToCharge());
    }

    @Test
    public void test_fixed_time_charge_rule(){
        //prepare
        FixedTimeChangePlan chargePlan = new FixedTimeChangePlan();
        Account account = new Account(13681874561L, Money.of(200), Collections.singletonList(chargePlan));
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 180);
        ctx.account = account;
        System.out.println("Account before charge: "+ account);

        //do
        FixedTimeChargeRule fixedTimeChargeRule = new FixedTimeChargeRule();
        fixedTimeChargeRule.belongsTo(chargePlan);
        fixedTimeChargeRule.doCharge(ctx);

        //check
        System.out.println("Account after charge: "+ account);
        Assertions.assertEquals( true, chargePlan.getResource().isCallingTimeRemaining());
        Assertions.assertEquals( 0, ctx.getDurationToCharge());

        // come a new charge
        ChargeContext ctx2 = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 40);
        ctx2.account = account;
        fixedTimeChargeRule.doCharge(ctx2);
        Assertions.assertEquals( false, chargePlan.getResource().isCallingTimeRemaining());
        Assertions.assertEquals( 20, ctx2.getDurationToCharge());

        //reset fixed time
        FixedTimeChangePlan.FreeCallTime.FREE_CALLED_TIME = 200;
        FixedTimeChangePlan.FreeCallTime.FREE_CALLING_TIME = 200;
    }
}
