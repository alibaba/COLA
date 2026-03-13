#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

import ${package}.Application;
import ${package}.domain.account.Account;
import ${package}.domain.charge.*;
import ${package}.domain.charge.chargeplan.BasicChargePlan;
import ${package}.domain.charge.chargeplan.ChargePlan;
import ${package}.domain.charge.chargeplan.FamilyChargePlan;
import ${package}.domain.charge.chargeplan.FixedTimeChangePlan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class CompositeChargeRuleTestRecord {

    private long callingPhoneNo = 13681874561L;
    private long calledPhoneNo = 15921582125L;

    @Test
    public void test_basic_and_fixedTime_charge_rule(){
        // prepare
        List<ChargePlan> chargePlanList = new ArrayList<>();
        chargePlanList.add(new BasicChargePlan());
        chargePlanList.add(new FixedTimeChangePlan());
        Account account = Account.valueOf(13681874561L, Money.of(200)); // for spring bean
        account.setChargePlanList(chargePlanList);
        ChargeContext ctx = new ChargeContext(CallType.CALLING, 13681874561L, 15921582125L, 220);
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, callingPhoneNo, calledPhoneNo);
        ctx.setSession(session);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);
        // check
        Assertions.assertEquals(2, chargeRecords.size());
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
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, callingPhoneNo, calledPhoneNo);
        ctx.setSession(session);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);
        // check
        Assertions.assertEquals(1, chargeRecords.size());
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
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, callingPhoneNo, calledPhoneNo);
        ctx.setSession(session);
        ctx.account = account;

        // do
        List<ChargeRecord> chargeRecords = account.charge(ctx);
        System.out.println("Account after charge: "+ account);

        // check
        Assertions.assertEquals(1, chargeRecords.size());
    }
}
