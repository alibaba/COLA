package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.ColaException;
import com.alibaba.cola.exception.DefaultExceptionHandler;
import com.alibaba.cola.exception.ExceptionHandlerFactory;
import com.alibaba.cola.exception.ExceptionHandlerI;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.google.common.collect.FluentIterable;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommandInvocation{

    private static Logger logger = LoggerFactory.getLogger(CommandInvocation.class);

    @Setter
    private CommandExecutorI commandExecutor;
    @Setter
    private Iterable<CommandInterceptorI> preInterceptors;
    @Setter
    private Iterable<CommandInterceptorI> postInterceptors;

    @Autowired
    private CommandHub commandHub;


    public CommandInvocation() {
        
    }
    
    public CommandInvocation(CommandExecutorI commandExecutor, List<CommandInterceptorI> preInterceptors,
                             List<CommandInterceptorI> postInterceptors){
        this.commandExecutor = commandExecutor;
        this.preInterceptors = preInterceptors;
        this.postInterceptors = postInterceptors;
    }

    public Response invoke(Command command) {
        Response response = null;
        try {
            preIntercept(command);
            response = commandExecutor.execute(command);  
        }
        catch(Exception e){
            response = getResponseInstance(command);
            response.setSuccess(false);
            ExceptionHandlerFactory.getExceptionHandler().handleException(command, response, e);
        }
        finally {
            //make sure post interceptors performs even though exception happens
            postIntercept(command, response);
        }          
        return response;
    }

    private void postIntercept(Command command, Response response) {
        try {
            for (CommandInterceptorI postInterceptor : FluentIterable.from(postInterceptors).toSet()) {
                postInterceptor.postIntercept(command, response);
            }
        }
        catch(Exception e){
            logger.error("postInterceptor error:"+e.getMessage(), e);
        }
    }

    private void preIntercept(Command command) {
        for (CommandInterceptorI preInterceptor : FluentIterable.from(preInterceptors).toSet()) {
            preInterceptor.preIntercept(command);
        }
    }

    private Response getResponseInstance(Command cmd) {
        Class responseClz = commandHub.getResponseRepository().get(cmd.getClass());
        try {
            return (Response) responseClz.newInstance();
        } catch (Exception e) {
            return new Response();
        }
    }
}
