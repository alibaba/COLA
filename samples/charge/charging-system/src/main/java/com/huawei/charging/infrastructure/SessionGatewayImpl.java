package com.huawei.charging.infrastructure;

import com.huawei.charging.domain.BizException;
import com.huawei.charging.domain.charge.Session;
import com.huawei.charging.domain.gateway.SessionGateway;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionGatewayImpl implements SessionGateway {
    private Map<String, Session> sessionMap = new HashMap<>();

    @Override
    public void create(Session session) {
        sessionMap.put(session.getSessionId(), session);
    }

    @Override
    public Session get(String sessionId) {
        return sessionMap.get(sessionId);
    }

    @Override
    public void end(String sessionId) {
        //真实场景是逻辑删除，比如把session的状态标记为“已结束”。
        sessionMap.remove(sessionId);
    }
}
