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
import com.alibaba.cola.event.EventI;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.cola.event.EventHub;
import com.alibaba.cola.exception.framework.ColaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * EventRegister
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventRegister{

    @Resource
    private EventHub eventHub;

    private Class<? extends EventI> getEventFromExecutor(Class<?> eventExecutorClz) {
        Method[] methods = eventExecutorClz.getDeclaredMethods();
        for (Method method : methods) {
            if (isExecuteMethod(method)){
                return checkAndGetEventParamType(method);
            }
        }
        throw new ColaException("Event param in " + eventExecutorClz + " " + ColaConstant.EXE_METHOD
                                 + "() is not detected");
    }

    public void doRegistration(EventHandlerI eventHandler){
        Class<? extends EventI> eventClz = getEventFromExecutor(eventHandler.getClass());
        eventHub.register(eventClz, eventHandler);
    }

    private boolean isExecuteMethod(Method method){
        return ColaConstant.EXE_METHOD.equals(method.getName()) && !method.isBridge();
    }

    private Class checkAndGetEventParamType(Method method){
        Class<?>[] exeParams = method.getParameterTypes();
        if (exeParams.length == 0){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should at least have one parameter");
        }
        if(!EventI.class.isAssignableFrom(exeParams[0]) ){
            throw new ColaException("Execute method in "+method.getDeclaringClass()+" should be the subClass of Event");
        }
        return exeParams[0];
    }
}
