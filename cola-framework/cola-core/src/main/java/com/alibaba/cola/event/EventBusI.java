package com.alibaba.cola.event;

import com.alibaba.cola.dto.event.Event;
import com.alibaba.cola.dto.Response;


/**
 * EventBus interface
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventBusI {

    /**
     * Send event to EventBus
     * 
     * @param event
     * @return Response
     */
    public void fire(Event event);

    /**
     * Send event to EventBus
     *
     * @param event
     * @return Response
     */
    public void asyncFire(Event event);

}
