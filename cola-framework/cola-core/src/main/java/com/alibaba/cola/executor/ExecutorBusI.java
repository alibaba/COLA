package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;

/**
 * 
 * CommandBus
 * 
 * @author fulan.zjf 2017年10月21日 下午11:00:58
 */
public interface ExecutorBusI {

    /**
     * Send command to CommandBus, then the command will be executed by CommandExecutor
     * 
     * @param command
     * @return Response
     */
    public <T extends Response> T send(Executor<T> command);

}
