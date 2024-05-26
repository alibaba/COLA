#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application;

import ${package}.application.dto.*;

public interface ChargeServiceI {
    Response begin(BeginSessionRequest request);

    Response charge(ChargeRequest request);

    Response end(EndSessionRequest request);

    MultiResponse<ChargeRecordDto> listChargeRecords(String sessionId);
}
