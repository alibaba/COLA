package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.framework.ExceptionHandlerFactory;
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
public class ExecutorInvocation {

    private static Logger logger = LoggerFactory.getLogger(ExecutorInvocation.class);

    @Setter
    private ExecutorI commandExecutor;
    @Setter
    private Iterable<ExecutorInterceptorI> preInterceptors;
    @Setter
    private Iterable<ExecutorInterceptorI> postInterceptors;

    @Autowired
    private ExecutorHub executorHub;


    public ExecutorInvocation() {
        
    }
    
    public ExecutorInvocation(ExecutorI commandExecutor, List<ExecutorInterceptorI> preInterceptors,
                              List<ExecutorInterceptorI> postInterceptors){
        this.commandExecutor = commandExecutor;
        this.preInterceptors = preInterceptors;
        this.postInterceptors = postInterceptors;
    }

    public Response invoke(Executor executor) {
        Response response = null;
        try {
            preIntercept(executor);
            response = commandExecutor.execute(executor);
        }
        catch(Exception e){
            response = getResponseInstance(executor);
            response.setSuccess(false);
            ExceptionHandlerFactory.getExceptionHandler().handleException(executor, response, e);
        }
        finally {
            //make sure post interceptors performs even though exception happens
            postIntercept(executor, response);
        }          
        return response;
    }

    private void postIntercept(Executor executor, Response response) {
        try {
            for (ExecutorInterceptorI postInterceptor : FluentIterable.from(postInterceptors).toSet()) {
                postInterceptor.postIntercept(executor, response);
            }
        }
        catch(Exception e){
            logger.error("postInterceptor error:"+e.getMessage(), e);
        }
    }

    private void preIntercept(Executor executor) {
        for (ExecutorInterceptorI preInterceptor : FluentIterable.from(preInterceptors).toSet()) {
            preInterceptor.preIntercept(executor);
        }
    }

    private Response getResponseInstance(Executor cmd) {
        Class responseClz = executorHub.getResponseRepository().get(cmd.getClass());
        try {
            return (Response) responseClz.newInstance();
        } catch (Exception e) {
            return new Response();
        }
    }
}
