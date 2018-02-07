/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.boot;

import com.alibaba.sofa.common.CoreConstant;
import com.alibaba.sofa.dto.event.Event;
import com.alibaba.sofa.event.EventHandlerI;
import com.alibaba.sofa.event.EventHub;
import com.alibaba.sofa.exception.InfraException;
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
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class EventRegister implements RegisterI, ApplicationContextAware {

    @Autowired
    private EventHub eventHub;

    private ApplicationContext applicationContext;

    @Override
    public void doRegistration(Class<?> targetClz) {
        Class<? extends Event> eventClz = getEventFromExecutor(targetClz);
        EventHandlerI executor = (EventHandlerI) applicationContext.getBean(targetClz);
        eventHub.register(eventClz, executor);
    }

    private Class<? extends Event> getEventFromExecutor(Class<?> eventExecutorClz) {
        Method[] methods = eventExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            Class<?>[] exeParams = method.getParameterTypes();
            /**
             * This is for return right response type on exception scenarios
             */
            if (CoreConstant.EXE_METHOD.equals(method.getName()) && exeParams.length == 1
                && Event.class.isAssignableFrom(exeParams[0]) && !method.isBridge()) {
                eventHub.getResponseRepository().put(exeParams[0], method.getReturnType());
                return (Class<? extends Event>) exeParams[0];
            }
        }
        throw new InfraException("Event param in " + eventExecutorClz + " " + CoreConstant.EXE_METHOD
                                 + "() is not detected");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
