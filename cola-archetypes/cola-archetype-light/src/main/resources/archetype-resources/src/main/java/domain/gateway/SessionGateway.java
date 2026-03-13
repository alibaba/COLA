#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.gateway;

import ${package}.domain.charge.Session;

public interface SessionGateway {

    void create(Session session);

    Session get(String sessionId);

    void end(String sessionId);
}
