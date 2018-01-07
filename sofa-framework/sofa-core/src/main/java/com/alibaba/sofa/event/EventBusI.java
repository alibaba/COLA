package com.alibaba.sofa.event;

import com.alibaba.sofa.dto.event.Event;
import com.alibaba.sofa.dto.Response;


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
    public Response fire(Event event);
}
