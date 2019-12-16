/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.command.CommandHub;
import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.dto.Command;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * PreInterceptorRegister 
 * @author fulan.zjf 2017-11-04
 */
@Component
public class PreInterceptorRegister extends AbstractRegister {

    @Autowired
    private CommandHub commandHub;
    
    @Override
    public void doRegistration(Class<?> targetClz) {
        CommandInterceptorI commandInterceptor = (CommandInterceptorI) ApplicationContextHelper.getBean(targetClz);
        PreInterceptor preInterceptorAnn = targetClz.getDeclaredAnnotation(PreInterceptor.class);
        Class<? extends Command>[] supportClasses = preInterceptorAnn.commands();
        registerInterceptor(supportClasses, commandInterceptor);        
    }

    private void registerInterceptor(Class<? extends Command>[] supportClasses, CommandInterceptorI commandInterceptor) {
        if (null == supportClasses || supportClasses.length == 0) {
            commandHub.getGlobalPreInterceptors().add(commandInterceptor);
            order(commandHub.getGlobalPreInterceptors());
            return;
        }
    }
}
