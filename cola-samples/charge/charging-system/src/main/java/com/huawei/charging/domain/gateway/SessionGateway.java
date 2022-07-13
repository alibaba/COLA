package com.huawei.charging.domain.gateway;

import com.huawei.charging.domain.charge.Session;

public interface SessionGateway {

    void create(Session session);

    Session get(String sessionId);

    void end(String sessionId);
}
