package com.alibaba.craftsman.common.event;

import com.alibaba.cola.event.DomainEventI;
import com.alibaba.cola.event.EventBusI;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * DomainEventPublisher, this is for demo purpose, the Event is sent to EventBus
 *
 * Normally DomainEvent should be sent to Messaging Middleware
 *
 * @author Frank Zhang
 * @date 2019-01-04 11:05 AM
 */
@Component
public class DomainEventPublisher{

    @Resource
    private EventBusI eventBus;

    public void publish(DomainEventI domainEvent) {
        eventBus.fire(domainEvent);
    }
}