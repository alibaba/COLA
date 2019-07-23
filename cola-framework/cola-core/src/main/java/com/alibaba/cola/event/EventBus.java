package com.alibaba.cola.event;

import com.alibaba.cola.dto.event.Event;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event Bus
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventBus implements EventBusI {
    Logger logger = LoggerFactory.getLogger(EventBus.class);

    @Autowired
    private EventHub eventHub;

    @Override
    public void fire(Event event) {
        eventHub.getEventHandler(event.getClass()).stream().forEach(p -> {
            p.execute(event);
        });
    }

    @Override
    public void asyncFire(Event event) {
        // todo parallel stream is not async but blocked...
        eventHub.getEventHandler(event.getClass()).parallelStream().forEach(p -> {
            p.execute(event);
        });
    }

/*
    Exception will be thrown out

    private void handleException(EventHandlerI handler, Event event, Exception exception) {
        logger.error("Process event error, EventHandler:["+handler.getClass()+"] Event:"+event+" ErrorMessage:"+exception.getMessage(), exception);
    }
 */
}
