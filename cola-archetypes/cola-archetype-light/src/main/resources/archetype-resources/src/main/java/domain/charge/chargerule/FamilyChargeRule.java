#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.charge.chargerule;

import ${package}.domain.charge.ChargeRecord;
import ${package}.domain.charge.ChargeContext;
import ${package}.domain.charge.Money;
import ${package}.domain.charge.chargeplan.ChargePlanType;
import ${package}.domain.charge.chargeplan.FamilyChargePlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FamilyChargeRule extends AbstractChargeRule {

    @Override
    public ChargeRecord doCharge(ChargeContext ctx) {
        FamilyChargePlan familyChargePlan = (FamilyChargePlan) chargePlan;
        FamilyChargePlan.FamilyMember familyMember = familyChargePlan.getResource();
        if (familyMember.isMember(ctx.otherSidePhoneNo)) {
            log.debug("Family Charge plan for Account : " + ctx.account);
            ChargeRecord chargeRecord = new ChargeRecord(ctx.phoneNo, ctx.callType, ctx.durationToCharge, ChargePlanType.FAMILY, Money.of(0));
            ctx.setDurationToCharge(0);
            return chargeRecord;
        }
        return null;
    }
}
