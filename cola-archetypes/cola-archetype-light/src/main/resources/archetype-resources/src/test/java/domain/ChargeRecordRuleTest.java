#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

import ${package}.domain.account.Account;
import ${package}.domain.charge.CallType;
import ${package}.domain.charge.ChargeContext;
import ${package}.domain.charge.Money;
import ${package}.domain.charge.chargeplan.BasicChargePlan;
import ${package}.domain.charge.chargeplan.ChargePlan;
import ${package}.domain.charge.chargeplan.FamilyChargePlan;
import ${package}.domain.charge.chargeplan.FixedTimeChangePlan;
import ${package}.domain.charge.chargerule.BasicChargeRule;
import ${package}.domain.charge.chargerule.FamilyChargeRule;
import ${package}.domain.charge.chargerule.FixedTimeChargeRule;
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
