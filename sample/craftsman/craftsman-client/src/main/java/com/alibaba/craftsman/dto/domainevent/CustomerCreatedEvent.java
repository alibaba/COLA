package com.alibaba.craftsman.dto.domainevent;

import com.alibaba.cola.event.DomainEventI;

/**
 * CustomerCreatedEvent
 *
 * @author Frank Zhang
 * @date 2019-01-04 10:32 AM
 */
public class CustomerCreatedEvent implements DomainEventI {

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
