#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.util;

import com.alibaba.cola.domain.DomainEventServiceI;
import com.alibaba.cola.dto.event.DomainEvent;
import com.alibaba.cola.event.EventBusI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DomainEventPublisher, this is for demo purpose, the Event is sent to EventBus
 *
 * Normally DomainEvent should be sent to Messaging Middleware
 *
 * @author Frank Zhang
 * @date 2019-01-04 11:05 AM
 */
@Component
public class DomainEventPublisher implements DomainEventServiceI{

    @Autowired
    private EventBusI eventBus;

    @Override
    public void publish(DomainEvent domainEvent) {
        eventBus.fire(domainEvent);
    }
}