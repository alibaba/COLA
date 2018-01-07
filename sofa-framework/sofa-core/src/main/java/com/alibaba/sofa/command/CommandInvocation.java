package com.alibaba.sofa.command;

import com.alibaba.sofa.dto.Command;
import com.alibaba.sofa.dto.Response;
import com.google.common.collect.FluentIterable;
import lombok.Setter;

import java.util.List;

public class CommandInvocation<R extends Response, C extends Command> {
    
    @Setter
    private CommandExecutorI<R,C> commandExecutor;
    @Setter
    private Iterable<CommandInterceptorI> preInterceptors;
    @Setter
    private Iterable<CommandInterceptorI> postInterceptors;
    
    public CommandInvocation() {
        
    }
    
    public CommandInvocation(CommandExecutorI<R, C> commandExecutor, List<CommandInterceptorI> preInterceptors,
                             List<CommandInterceptorI> postInterceptors){
        this.commandExecutor = commandExecutor;
        this.preInterceptors = preInterceptors;
        this.postInterceptors = postInterceptors;
    }

    public R invoke(C command) {
        preIntercept(command);
        R response = null;
        try {
            response = commandExecutor.execute(command);  
            response.setSuccess(true);
        }
        finally {
            //make sure post interceptors performs even though exception happens
            postIntercept(command, response);     
        }          
        return response;
    }

    private void postIntercept(C command, R response) {
        for (CommandInterceptorI postInterceptor : FluentIterable.from(postInterceptors).toSet()) {
            postInterceptor.postIntercept(command, response);
        }
    }

    private void preIntercept(C command) {
        for (CommandInterceptorI preInterceptor : FluentIterable.from(preInterceptors).toSet()) {
            preInterceptor.preIntercept(command);
        }
    }
}
