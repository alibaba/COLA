#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dto.domainevent;

import com.alibaba.cola.dto.DomainEvent;

import static ${package}.dto.domainevent.DomainEventConstant.CUSTOMER_CREATED_TOPIC;

/**
 * CustomerCreatedEvent
 *
 * @author Frank Zhang
 * @date 2019-01-04 10:32 AM
 */
public class CustomerCreatedEvent extends DomainEvent {

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getEventTopic() {
        return CUSTOMER_CREATED_TOPIC;
    }
}
