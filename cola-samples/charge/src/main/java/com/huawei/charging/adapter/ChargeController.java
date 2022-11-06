package com.huawei.charging.adapter;

import com.huawei.charging.application.ChargeServiceI;
import com.huawei.charging.application.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class ChargeController {

    @Resource
    private ChargeServiceI chargeService;

    @PostMapping("session/{sessionId}/begin")
    public Response begin(@PathVariable(name = "sessionId") String sessionId,
                          @RequestParam long callingPhoneNo,
                          @RequestParam long calledPhoneNo) {
        log.debug(sessionId + " " + callingPhoneNo + " " + calledPhoneNo);
        BeginSessionRequest request = new BeginSessionRequest(sessionId, callingPhoneNo, calledPhoneNo);
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
