/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.executor.ExecutorHub;
import com.alibaba.cola.executor.ExecutorInterceptorI;
import com.alibaba.cola.executor.PreInterceptor;
import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.dto.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PreInterceptorRegister 
 * @author fulan.zjf 2017-11-04
 */
@Component
public class PreInterceptorRegister extends AbstractRegister {

    @Autowired
    private ExecutorHub executorHub;
    
    @Override
    public void doRegistration(Class<?> targetClz) {
        ExecutorInterceptorI commandInterceptor = (ExecutorInterceptorI) ApplicationContextHelper.getBean(targetClz);
        PreInterceptor preInterceptorAnn = targetClz.getDeclaredAnnotation(PreInterceptor.class);
        Class<? extends Executor>[] supportClasses = preInterceptorAnn.commands();
        registerInterceptor(supportClasses, commandInterceptor);        
    }

    private void registerInterceptor(Class<? extends Executor>[] supportClasses, ExecutorInterceptorI commandInterceptor) {
        if (null == supportClasses || supportClasses.length == 0) {
            executorHub.getGlobalPreInterceptors().add(commandInterceptor);
            order(executorHub.getGlobalPreInterceptors());
            return;
        }
    }

    @Override
    public Class annotationType() {
        return PreInterceptor.class;
    }
}
