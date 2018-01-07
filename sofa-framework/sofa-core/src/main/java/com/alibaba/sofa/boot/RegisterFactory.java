/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.boot;

import com.alibaba.sofa.command.Command;
import com.alibaba.sofa.command.PostInterceptor;
import com.alibaba.sofa.command.PreInterceptor;
import com.alibaba.sofa.common.CoreConstant;
import com.alibaba.sofa.event.EventHandler;
import com.alibaba.sofa.exception.InfraException;
import com.alibaba.sofa.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RegisterFactory
 *
 * @author fulan.zjf 2017-11-04
 */
@Component
public class RegisterFactory{

    @Autowired
    private PreInterceptorRegister preInterceptorRegister;
    @Autowired
    private PostInterceptorRegister postInterceptorRegister;
    @Autowired
    private CommandRegister commandRegister;
    @Autowired
    private ExtensionRegister extensionRegister;
    @Autowired
    private EventRegister eventRegister;
    @Autowired
    private PlainValidatorRegister plainValidatorRegister;
    @Autowired
    private PlainRuleRegister plainRuleRegister;

    public RegisterI getRegister(Class<?> targetClz) {
        PreInterceptor preInterceptorAnn = targetClz.getDeclaredAnnotation(PreInterceptor.class);
        if (preInterceptorAnn != null) {
            return preInterceptorRegister;
        }
        PostInterceptor postInterceptorAnn = targetClz.getDeclaredAnnotation(PostInterceptor.class);
        if (postInterceptorAnn != null) {
            return postInterceptorRegister;
        }
        Command commandAnn = targetClz.getDeclaredAnnotation(Command.class);
        if (commandAnn != null) {
            return commandRegister;
        }
        Extension extensionAnn = targetClz.getDeclaredAnnotation(Extension.class);
        if (extensionAnn != null) {
            return extensionRegister;
        }
        if (isPlainValidator(targetClz)) {
            return plainValidatorRegister;
        }
        if (isPlainRule(targetClz)) {
            return plainRuleRegister;
        }
        EventHandler eventHandlerAnn = targetClz.getDeclaredAnnotation(EventHandler.class);
        if (eventHandlerAnn != null) {
            return eventRegister;
        }
        return null;
    }

    private boolean isPlainRule(Class<?> targetClz) {
        if (ClassInterfaceChecker.check(targetClz, CoreConstant.RULEI_CLASS) && makeSureItsNotExtensionPoint(targetClz)) {
            return true;
        }
        return false;
    }

    private boolean isPlainValidator(Class<?> targetClz) {
        if (ClassInterfaceChecker.check(targetClz, CoreConstant.VALIDATORI_CLASS) && makeSureItsNotExtensionPoint(targetClz)) {
            return true;
        }
        return false;
    }

    private boolean makeSureItsNotExtensionPoint(Class<?> targetClz) {
        if (ClassInterfaceChecker.check(targetClz, CoreConstant.EXTENSIONPOINT_CLASS)) {
            throw new InfraException(
                "Please add @Extension for " + targetClz.getSimpleName() + " since it's a ExtensionPoint");
        }
        return true;
    }
}
