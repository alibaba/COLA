#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.charge.chargerule;

import ${package}.domain.charge.ChargeRecord;
import ${package}.domain.charge.ChargeContext;
import ${package}.domain.charge.chargeplan.ChargePlan;

public interface ChargeRule {
    ChargeRecord doCharge(ChargeContext ctx);

    void belongsTo(ChargePlan chargePlan);

}
