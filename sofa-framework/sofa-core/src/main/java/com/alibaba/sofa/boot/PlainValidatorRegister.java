package com.alibaba.sofa.boot;

import com.alibaba.sofa.validator.PlainValidatorRepository;
import com.alibaba.sofa.validator.ValidatorI;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Plain Validator is Validator Component without ExtensionPoint
 * @author fulan.zjf
 * @date 2017/12/21
 */
@Component
public class PlainValidatorRegister implements RegisterI, ApplicationContextAware {

    @Autowired
    private PlainValidatorRepository plainValidatorRepository;

    private ApplicationContext applicationContext;

    @Override
    public void doRegistration(Class<?> targetClz) {
        ValidatorI plainValidator= (ValidatorI) applicationContext.getBean(targetClz);
        plainValidatorRepository.getPlainValidators().put(plainValidator.getClass(), plainValidator);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
