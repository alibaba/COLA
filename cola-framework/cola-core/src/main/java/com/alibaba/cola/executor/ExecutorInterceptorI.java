package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;

/**
 * Interceptor will do AOP processing before or after Command Execution
 * 
 * @author fulan.zjf 2017年10月25日 下午4:04:43
 */
public interface ExecutorInterceptorI {
   
   /**
    * Pre-processing before command execution
    * @param executor
    */
   default public void preIntercept(Executor executor){};
   
   /**
    * Post-processing after command execution
    * @param executor
    * @param response, Note that response could be null, check it before use
    */
   default public void postIntercept(Executor executor, Response response){};

}
