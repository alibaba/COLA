/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.executor.*;
import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.dto.Executor;
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
public class ExecutorRegister implements RegisterI {

    @Autowired
    private ExecutorHub executorHub;

    @Override
    public void doRegistration(Class<?> targetClz) {
        Class<? extends Executor> commandClz = getCommandFromExecutor(targetClz);
        ExecutorInvocation executorInvocation = ApplicationContextHelper.getBean(ExecutorInvocation.class);
        executorInvocation.setCommandExecutor((ExecutorI) ApplicationContextHelper.getBean(targetClz));
        executorInvocation.setPreInterceptors(collectInterceptors(commandClz, true));
        executorInvocation.setPostInterceptors(collectInterceptors(commandClz, false));
        executorHub.getCommandRepository().put(commandClz, executorInvocation);
    }

    private Class<? extends Executor> getCommandFromExecutor(Class<?> commandExecutorClz) {
        Method[] methods = commandExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            if (isExecuteMethod(method)){
                Class commandClz = checkAndGetCommandParamType(method);
                executorHub.getResponseRepository().put(commandClz, method.getReturnType());
                return (Class<? extends Executor>) commandClz;
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
        if(!Executor.class.isAssignableFrom(exeParams[0]) ){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should be the subClass of Command");
        }
        return exeParams[0];
    }

    private Iterable<ExecutorInterceptorI> collectInterceptors(Class<? extends Executor> commandClass, boolean pre) {
        /**
         * add 通用的Interceptors
         */
        Iterable<ExecutorInterceptorI> commandItr = Iterables.concat((pre ? executorHub.getGlobalPreInterceptors() : executorHub.getGlobalPostInterceptors()));

        return commandItr;
    }

    @Override
    public Class annotationType() {
        return com.alibaba.cola.executor.Executor.class;
    }
}
