package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;

/**
 * 
 * CommandBus
 * 
 * @author fulan.zjf 2017年10月21日 下午11:00:58
 */
public interface CommandBusI {

    /**
     * Send command to CommandBus, then the command will be executed by CommandExecutor
     * 
     * @param Command or Query
     * @return Response
     */
    public Response send(Command cmd);
}
