#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.dto;

import ${package}.domain.charge.CallType;
import ${package}.domain.charge.ChargeRecord;
import ${package}.domain.charge.chargeplan.ChargePlanType;

public class ChargeRecordDto {
    public Long id;
    public String sessionId;
    public long phoneNo;
    public int chargeDuration;
    public long cost;
    public CallType callType;
    public ChargePlanType chargePlanType;

    public static ChargeRecordDto fromEntity(ChargeRecord chargeRecord){
        ChargeRecordDto dto = new ChargeRecordDto();
        dto.id = chargeRecord.getId();
        dto.sessionId = chargeRecord.getSessionId();
        dto.phoneNo = chargeRecord.getPhoneNo();
        dto.chargeDuration = chargeRecord.getChargeDuration();
        dto.cost = chargeRecord.getCost().getAmount();
        dto.callType = chargeRecord.getCallType();
        dto.chargePlanType = chargeRecord.getChargePlanType();
        return dto;
    }
}
