package com.alibaba.cola.event;

import com.alibaba.cola.dto.Response;

import java.util.concurrent.ExecutorService;

/**
 * event handler
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
public interface EventHandlerI<R extends Response, E extends EventI> {

    default public ExecutorService getExecutor(){
        return null;
    }

    public R execute(E e);
}
