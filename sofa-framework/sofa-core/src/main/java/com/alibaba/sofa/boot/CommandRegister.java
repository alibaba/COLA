/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.boot;

import com.alibaba.sofa.command.CommandExecutorI;
import com.alibaba.sofa.command.CommandHub;
import com.alibaba.sofa.command.CommandInterceptorI;
import com.alibaba.sofa.command.CommandInvocation;
import com.alibaba.sofa.common.CoreConstant;
import com.alibaba.sofa.dto.Command;
import com.alibaba.sofa.exception.InfraException;
import com.google.common.collect.Iterables;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * CommandRegister
 * 
 * @author fulan.zjf 2017-11-04
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class CommandRegister implements RegisterI, ApplicationContextAware {

    @Autowired
    private CommandHub         commandHub;

    private ApplicationContext applicationContext;

    @Override
    public void doRegistration(Class<?> targetClz) {
        Class<? extends Command> commandClz = getCommandFromExecutor(targetClz);
        CommandInvocation commandInvocation = new CommandInvocation();
        commandInvocation.setCommandExecutor((CommandExecutorI) applicationContext.getBean(targetClz));
        commandInvocation.setPreInterceptors(collectInterceptors(commandClz, true));
        commandInvocation.setPostInterceptors(collectInterceptors(commandClz, false));
        commandHub.getCommandRepository().put(commandClz, commandInvocation);
    }

    private Class<? extends Command> getCommandFromExecutor(Class<?> commandExecutorClz) {
        Method[] methods = commandExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            Class<?>[] exeParams = method.getParameterTypes();
            /**
             * This is for return right response type on exception scenarios
             */
            if (CoreConstant.EXE_METHOD.equals(method.getName()) && exeParams.length == 1
                && Command.class.isAssignableFrom(exeParams[0]) && !method.isBridge()) {
                commandHub.getResponseRepository().put(exeParams[0], method.getReturnType());
                return (Class<? extends Command>) exeParams[0];
            }
        }
        throw new InfraException("Command param in " + commandExecutorClz + " " + CoreConstant.EXE_METHOD
                                 + "() is not detected");
    }

    private Iterable<CommandInterceptorI> collectInterceptors(Class<? extends Command> commandClass, boolean pre) {
        /**
         * add 通用的Interceptors
         */
        Iterable<CommandInterceptorI> commandItr = Iterables.concat((pre ? commandHub.getGlobalPreInterceptors() : commandHub.getGlobalPostInterceptors()));
        /**
         * add command自己专属的Interceptors
         */
        Iterables.concat(commandItr, (pre ? commandHub.getPreInterceptors() : commandHub.getPostInterceptors()).get(commandClass));
        /**
         * add parents的Interceptors
         */
        Class<?> superClass = commandClass.getSuperclass();
        while (Command.class.isAssignableFrom(superClass)) {
            Iterables.concat(commandItr, (pre ? commandHub.getPreInterceptors() : commandHub.getPostInterceptors()).get(commandClass));
            superClass = superClass.getSuperclass();
        }
        return commandItr;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
