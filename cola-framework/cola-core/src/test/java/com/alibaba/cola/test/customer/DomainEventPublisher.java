package com.alibaba.cola.test.customer;

import com.alibaba.cola.event.DomainEventI;
import com.alibaba.cola.event.EventBusI;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * DomainEventPublisher
 *
 * @author Frank Zhang
 * @date 2020-06-22 7:04 PM
 */
@Component
public class DomainEventPublisher {
    @Resource
    private EventBusI eventBus;

    public void publish(DomainEventI domainEvent) {
        eventBus.fire(domainEvent);
    }
}
