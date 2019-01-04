/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.event.Event;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.cola.event.EventHub;
import com.alibaba.cola.exception.ColaException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * EventRegister
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventRegister implements RegisterI {

    @Autowired
    private EventHub eventHub;

    @Override
    public void doRegistration(Class<?> targetClz) {
        Class<? extends Event> eventClz = getEventFromExecutor(targetClz);
        EventHandlerI executor = (EventHandlerI) ApplicationContextHelper.getBean(targetClz);
        eventHub.register(eventClz, executor);
    }

    private Class<? extends Event> getEventFromExecutor(Class<?> eventExecutorClz) {
        Method[] methods = eventExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            if (isExecuteMethod(method)){
                return checkAndGetEventParamType(method);
            }
        }
        throw new ColaException("Event param in " + eventExecutorClz + " " + ColaConstant.EXE_METHOD
                                 + "() is not detected");
    }

    private boolean isExecuteMethod(Method method){
        return ColaConstant.EXE_METHOD.equals(method.getName()) && !method.isBridge();
    }

    private Class checkAndGetEventParamType(Method method){
        Class<?>[] exeParams = method.getParameterTypes();
        if (exeParams.length == 0){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should at least have one parameter");
        }
        if(!Event.class.isAssignableFrom(exeParams[0]) ){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should be the subClass of Event");
        }
        return exeParams[0];
    }
}
