/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.command.CommandHub;
import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.CommandInvocation;
import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.exception.framework.ColaException;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * CommandRegister
 * 
 * @author fulan.zjf 2017-11-04
 */

@Component
public class CommandRegister implements RegisterI {

    @Autowired

    private CommandHub         commandHub;

    @Override
    public void doRegistration(Class<?> targetClz) {
        Class<? extends Command> commandClz = getCommandFromExecutor(targetClz);
        CommandInvocation commandInvocation = ApplicationContextHelper.getBean(CommandInvocation.class);
        commandInvocation.setCommandExecutor((CommandExecutorI) ApplicationContextHelper.getBean(targetClz));
        commandInvocation.setPreInterceptors(collectInterceptors(commandClz, true));
        commandInvocation.setPostInterceptors(collectInterceptors(commandClz, false));
        commandHub.getCommandRepository().put(commandClz, commandInvocation);
    }

    private Class<? extends Command> getCommandFromExecutor(Class<?> commandExecutorClz) {
        Method[] methods = commandExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            if (isExecuteMethod(method)){
                Class commandClz = checkAndGetCommandParamType(method);
                commandHub.getResponseRepository().put(commandClz, method.getReturnType());
                return (Class<? extends Command>) commandClz;
            }
        }
        throw new ColaException(" There is no " + ColaConstant.EXE_METHOD + "() in "+ commandExecutorClz);
    }

    private boolean isExecuteMethod(Method method ){
        return ColaConstant.EXE_METHOD.equals(method.getName()) && !method.isBridge();
    }

    private Class checkAndGetCommandParamType(Method method){
        Class<?>[] exeParams = method.getParameterTypes();
        if (exeParams.length == 0){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should at least have one parameter");
        }
        if(!Command.class.isAssignableFrom(exeParams[0]) ){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should be the subClass of Command");
        }
        return exeParams[0];
    }

    private Iterable<CommandInterceptorI> collectInterceptors(Class<? extends Command> commandClass, boolean pre) {
        /**
         * add 通用的Interceptors
         */
        Iterable<CommandInterceptorI> commandItr = Iterables.concat((pre ? commandHub.getGlobalPreInterceptors() : commandHub.getGlobalPostInterceptors()));

        return commandItr;
    }

}
