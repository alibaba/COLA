package com.alibaba.craftsman.interceptor;

import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;

@PreInterceptor
public class LoggerPreInterceptor implements CommandInterceptorI{

    Logger logger = LoggerFactory.getLogger(LoggerPreInterceptor.class);

    @Override
    public void preIntercept(Command command) {
        logger.debug("Start processing " + command+"; Context: ");
    }

}
