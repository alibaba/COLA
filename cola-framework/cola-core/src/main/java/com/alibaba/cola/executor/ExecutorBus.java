package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Just send Command to CommandBus, 
 * 
 * @author fulan.zjf 2017年10月24日 上午12:47:18
 */
@Component
public class ExecutorBus implements ExecutorBusI {

    @Autowired
    private ExecutorHub executorHub;

    @Override
    public Response send(Executor cmd) {
            return executorHub.getCommandInvocation(cmd.getClass()).invoke(cmd);
    }

}
