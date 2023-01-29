package com.huawei.charging.ut;

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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        Assert.assertEquals( Money.of(100), ctx.account.getRemaining());
        Assert.assertEquals( 0, ctx.getDurationToCharge());
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
        Assert.assertEquals( Money.of(200), ctx.account.getRemaining());
        Assert.assertEquals( 0, ctx.getDurationToCharge());
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
        Assert.assertEquals( true, chargePlan.getResource().isCallingTimeRemaining());
        Assert.assertEquals( 0, ctx.getDurationToCharge());

        // come a new charge
        ChargeContext ctx2 = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 40);
        ctx2.account = account;
        fixedTimeChargeRule.doCharge(ctx2);
        Assert.assertEquals( false, chargePlan.getResource().isCallingTimeRemaining());
        Assert.assertEquals( 20, ctx2.getDurationToCharge());

        //reset fixed time
        FixedTimeChangePlan.FreeCallTime.FREE_CALLED_TIME = 200;
        FixedTimeChangePlan.FreeCallTime.FREE_CALLING_TIME = 200;
    }
}
