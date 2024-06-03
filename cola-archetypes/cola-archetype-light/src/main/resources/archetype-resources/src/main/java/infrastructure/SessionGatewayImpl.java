#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure;

import ${package}.domain.BizException;
import ${package}.domain.charge.Session;
import ${package}.domain.gateway.SessionGateway;
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
