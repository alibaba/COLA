package com.alibaba.cola.event;

import com.alibaba.cola.dto.event.Event;
import com.alibaba.cola.dto.Response;

/**
 * event handler
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventHandlerI<E extends Event> {

    public void execute(E e);
}
