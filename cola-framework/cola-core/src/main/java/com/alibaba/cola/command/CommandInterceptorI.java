package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;

/**
 * Interceptor will do AOP processing before or after Command Execution
 * 
 * @author fulan.zjf 2017年10月25日 下午4:04:43
 */
public interface CommandInterceptorI<T extends com.alibaba.cola.dto.Command> {
   
   /**
    * Pre-processing before command execution
    * @param command
    */
   default public void preIntercept(T command){};
   
   /**
    * Post-processing after command execution
    * @param command
    * @param response, Note that response could be null, check it before use
    */
   default public void postIntercept(T command, Response response){};

}
