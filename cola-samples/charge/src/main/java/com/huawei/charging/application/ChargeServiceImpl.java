package com.huawei.charging.application;

import com.huawei.charging.application.dto.*;
import com.huawei.charging.domain.account.Account;
import com.huawei.charging.domain.account.AccountDomainService;
import com.huawei.charging.domain.charge.CallType;
import com.huawei.charging.domain.charge.ChargeContext;
import com.huawei.charging.domain.charge.ChargeRecord;
import com.huawei.charging.domain.charge.Session;
import com.huawei.charging.domain.gateway.AccountGateway;
import com.huawei.charging.domain.gateway.ChargeGateway;
import com.huawei.charging.domain.gateway.SessionGateway;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChargeServiceImpl implements ChargeServiceI {

    @Resource
    private SessionGateway sessionGateway;

    @Resource
    private AccountGateway accountGateway;

    @Resource
    private AccountDomainService accountDomainService;

    @Resource
    private ChargeGateway chargeGateway;

    @Override
    public Response begin(BeginSessionRequest request) {
        Session session = request.toSession();
        accountDomainService.canSessionStart(session);
        sessionGateway.create(session);
        log.debug("Session created successfully :" + session);
        return Response.buildSuccess();
    }

    @Override
    public Response charge(ChargeRequest request) {
        log.debug("Do charge : " + request);
        Session session = sessionGateway.get(request.getSessionId());
        int durationToCharge = request.getDuration() - session.getChargedDuration();
        List<ChargeRecord> chargeRecordList = new ArrayList<>();
        chargeCalling(session, durationToCharge, chargeRecordList);
        chargeCalled(session, durationToCharge, chargeRecordList);
        chargeGateway.saveAll(chargeRecordList);
        session.setChargedDuration(request.getDuration());
        return Response.buildSuccess();
    }

    private void chargeCalling(Session session, int durationToCharge, List<ChargeRecord> chargeRecordList) {
        Account callingAccount = accountGateway.getAccount(session.getCallingPhoneNo());
        ChargeContext callingCtx = new ChargeContext(CallType.CALLING, session.getCallingPhoneNo(), session.getCalledPhoneNo(), durationToCharge);
        callingCtx.session = session;
        callingCtx.account = callingAccount;
        chargeRecordList.addAll(callingAccount.charge(callingCtx));
    }

    private void chargeCalled(Session session, int durationToCharge, List<ChargeRecord> chargeRecordList) {
        Account calledAccount = accountGateway.getAccount(session.getCalledPhoneNo());
        ChargeContext calledCtx = new ChargeContext(CallType.CALLED, session.getCalledPhoneNo(), session.getCallingPhoneNo(), durationToCharge);
        calledCtx.session = session;
        calledCtx.account = calledAccount;
        chargeRecordList.addAll(calledAccount.charge(calledCtx));
    }

    @Override
    public Response end(EndSessionRequest request) {
        charge(request.toChargeRequest());
        sessionGateway.end(request.getSessionId());
        return Response.buildSuccess();
    }

    @Override
    public MultiResponse<ChargeRecordDto> listChargeRecords(String sessionId) {
        List<ChargeRecord> chargeRecordList = chargeGateway.findBySessionId(sessionId);
        List<ChargeRecordDto> chargeRecordDtoList = new ArrayList<>();
        for (ChargeRecord chargeRecord : chargeRecordList) {
            chargeRecordDtoList.add(ChargeRecordDto.fromEntity(chargeRecord));
        }
        return MultiResponse.of(chargeRecordDtoList);
    }
}
