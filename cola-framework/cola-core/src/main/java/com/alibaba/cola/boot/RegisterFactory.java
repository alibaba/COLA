/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RegisterFactory
 *
 * @author lorne 2020-01-27
 * @author fulan.zjf 2017-11-04
 */
@Component
public class RegisterFactory{

    private List<RegisterI> registers;

    public RegisterFactory(@Autowired(required = false)
                                   List<RegisterI> registers) {
        this.registers = registers;
    }

    public RegisterI getRegister(Class<?> targetClz) {
        for(RegisterI register:registers){
            if(targetClz.getDeclaredAnnotation(register.annotationType())!=null){
                return register;
            }
        }
        return null;
    }
}
