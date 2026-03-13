#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.charge.chargerule;

import ${package}.domain.charge.chargeplan.ChargePlan;

public abstract class AbstractChargeRule implements ChargeRule{
    protected ChargePlan chargePlan;

    @Override
    public void belongsTo(ChargePlan chargePlan){
        this.chargePlan = chargePlan;
    }


}
