package com.alibaba.cola.event;

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
    public Response fire(EventI event);

    /**
     * fire all handlers which registed the event
     *
     * @param event
     * @return Response
     */
    public void fireAll(EventI event);

    /**
     * Async fire all handlers
     * @param event
     */
    public void asyncFire(EventI event);
}
