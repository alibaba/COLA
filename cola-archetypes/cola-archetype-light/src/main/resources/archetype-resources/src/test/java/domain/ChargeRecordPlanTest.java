#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;


import ${package}.domain.charge.chargeplan.BasicChargePlan;
import ${package}.domain.charge.chargeplan.ChargePlan;
import ${package}.domain.charge.chargeplan.ChargePlanType;
import ${package}.domain.charge.chargeplan.FamilyChargePlan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChargeRecordPlanTest {

    @Test
    public void test_priority(){
        ChargePlan basicChargePlan = new BasicChargePlan();
        ChargePlan familyChargePlan = new FamilyChargePlan();
        ChargePlan fixedTimeChargePlan = new FamilyChargePlan();
        List<ChargePlan> chargePlanList =  new ArrayList<>();
        chargePlanList.add(basicChargePlan);
        chargePlanList.add(familyChargePlan);
        chargePlanList.add(fixedTimeChargePlan);

        Collections.sort(chargePlanList);

        System.out.println(chargePlanList.get(0));
        Assertions.assertEquals(ChargePlanType.FAMILY, chargePlanList.get(0).getType());

    }
}
