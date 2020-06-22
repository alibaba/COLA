package com.alibaba.cola.test.customer;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.cola.event.EventI;

/**
 * CustomerCreatedEventHandler
 *
 * @author Frank Zhang
 * @date 2020-06-22 7:00 PM
 */
@EventHandler
public class CustomerCreatedEventHandler implements EventHandlerI<Response, CustomerCreatedEvent> {

    @Override
    public Response execute(CustomerCreatedEvent customerCreatedEvent) {
        System.out.println("customerCreatedEvent processed");
        return null;
    }
}
