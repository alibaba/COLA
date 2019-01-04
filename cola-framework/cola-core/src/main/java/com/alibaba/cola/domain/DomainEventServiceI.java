package com.alibaba.cola.domain;

import com.alibaba.cola.dto.event.DomainEvent;

/**
 * DomainEventServiceI
 *
 * @author Frank Zhang
 * @date 2019-01-03 12:25 PM
 */
public interface DomainEventServiceI {

    /**
     * Publish Domain Event
     *
     * The implementation should be provided by application, depends on what kind of Messaging mechanism you are using
     *
     * It could be RocketMQ, ActiveMQ, Kafka etc.
     * @param domainEvent
     */
    public void publish(DomainEvent domainEvent);
}
