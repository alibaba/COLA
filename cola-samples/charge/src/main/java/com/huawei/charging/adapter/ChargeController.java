package com.huawei.charging.adapter;

import com.huawei.charging.application.ChargeServiceI;
import com.huawei.charging.application.dto.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class ChargeController {

    @Resource
    private ChargeServiceI chargeService;

    @PostMapping("session/{sessionId}/begin")
    public Response begin(@PathVariable(name = "sessionId") String sessionId,
                          @RequestParam("callingPhoneNo") String callingPhoneNo,
                          @RequestParam("calledPhoneNo") String calledPhoneNo) {
        log.debug(sessionId + " " + callingPhoneNo + " " + calledPhoneNo);
        BeginSessionRequest request = new BeginSessionRequest(sessionId, Long.valueOf(callingPhoneNo), Long.valueOf(calledPhoneNo));
        return chargeService.begin(request);
    }

    @PostMapping("session/{sessionId}/charge")
    public Response charge(@PathVariable(name = "sessionId") String sessionId,
                       @RequestParam int duration) {
        log.debug(sessionId + " " + duration);
        ChargeRequest request = new ChargeRequest(sessionId, duration);
        return chargeService.charge(request);
    }

    @PostMapping("session/{sessionId}/end")
    public Response end(@PathVariable(name = "sessionId") String sessionId,
                    @RequestParam int duration) {
        log.debug(sessionId + " " + duration);
        EndSessionRequest request = new EndSessionRequest(sessionId, duration);
        return chargeService.end(request);
    }

    @GetMapping("{sessionId}/chargeRecords")
    public MultiResponse<ChargeRecordDto> getChargeRecord(@PathVariable(name = "sessionId") String sessionId) {
        return chargeService.listChargeRecords(sessionId);
    }
}
