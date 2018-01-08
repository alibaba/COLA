/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.validator;

import com.alibaba.sofa.extension.ExtensionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ValidatorExecutor
 * 
 * @author fulan.zjf 2017-11-04
 */
@Component
public class ValidatorExecutor extends ExtensionExecutor {

    @Autowired
    private PlainValidatorRepository plainValidatorRepository;

    public void validate(Class<? extends ValidatorI> targetClz, Object candidate) {
        ValidatorI validator = locateComponent(targetClz);
        validator.validate(candidate);
    }

    @Override
    protected <C> C locateComponent(Class<C> targetClz) {
        C validator = (C) plainValidatorRepository.getPlainValidators().get(targetClz);
        return null != validator ? validator : super.locateComponent(targetClz);
    }

}