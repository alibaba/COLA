package com.huawei.charging.ut;

import com.huawei.charging.Application;
import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.charge.CallType;
import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.ChargeContext;
import com.huawei.charging.domain.charge.Money;
import com.huawei.charging.domain.charge.chargeplan.BasicChargePlan;
import com.huawei.charging.domain.charge.chargeplan.ChargePlan;
import com.huawei.charging.domain.charge.chargeplan.FamilyChargePlan;
import com.huawei.charging.domain.charge.chargeplan.FixedTimeChangePlan;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class CompositeChargeRuleTestRecord {

    @Test
    public void test_basic_and_fixedTime_charge_rule(){
        // prepare
        List<ChargePlan> chargePlanList = new ArrayList<>();
        chargePlanList.add(new BasicChargePlan());
        chargePlanList.add(new FixedTimeChangePlan());
        Account account = Account.valueOf(13681874561L, Money.of(200)); // for spring bean
        account.setChargePlanList(chargePlanList);
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 220);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);
        // check
        Assert.assertEquals(2, chargeRecords.size());
    }

    @Test
    public void test_basic_and_family_charge_rule(){
        // prepare
        List<ChargePlan> chargePlanList = new ArrayList<>();
        chargePlanList.add(new BasicChargePlan());
        chargePlanList.add(new FamilyChargePlan());
        Account account = Account.valueOf(13681874561L, Money.of(200)); // for spring bean
        account.setChargePlanList(chargePlanList);
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 220);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);
        // check
        Assert.assertEquals(1, chargeRecords.size());
    }

    @Test
    public void test_all_charge_rule(){
        // prepare
        List<ChargePlan> chargePlanList = new ArrayList<>();
        chargePlanList.add(new BasicChargePlan());
        chargePlanList.add(new FamilyChargePlan());
        chargePlanList.add(new FixedTimeChangePlan());
        Account account = Account.valueOf(13681874561L, Money.of(200)); // for spring bean
        account.setChargePlanList(chargePlanList);
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 220);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);

        // check
        Assert.assertEquals(1, chargeRecords.size());
    }
}
