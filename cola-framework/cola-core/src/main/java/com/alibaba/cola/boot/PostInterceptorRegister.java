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
import com.alibaba.cola.executor.PostInterceptor;
import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.dto.Executor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * PostInterceptorRegister 
 * @author fulan.zjf 2017-11-04
 */
@Component
public class PostInterceptorRegister extends AbstractRegister {

    @Autowired
    private ExecutorHub executorHub;
    
    @Override
    public void doRegistration(Class<?> targetClz) {
        ExecutorInterceptorI commandInterceptor = (ExecutorInterceptorI) ApplicationContextHelper.getBean(targetClz);
        PostInterceptor postInterceptorAnn = targetClz.getDeclaredAnnotation(PostInterceptor.class);
        Class<? extends Executor>[] supportClasses = postInterceptorAnn.commands();
        registerInterceptor(supportClasses, commandInterceptor);        
    }

    private void registerInterceptor(Class<? extends Executor>[] supportClasses, ExecutorInterceptorI commandInterceptor) {
        if (null == supportClasses || supportClasses.length == 0) {
            executorHub.getGlobalPostInterceptors().add(commandInterceptor);
            order(executorHub.getGlobalPostInterceptors());
            return;
        }
    }    

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class annotationType() {
        return PostInterceptor.class;
    }
}
