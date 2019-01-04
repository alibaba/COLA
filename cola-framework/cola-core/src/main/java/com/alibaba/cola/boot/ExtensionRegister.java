/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.boot;

import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.exception.ColaException;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.extension.ExtensionCoordinate;
import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.extension.ExtensionRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ExtensionRegister 
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionRegister implements RegisterI, ApplicationContextAware{

    @Autowired
    private ExtensionRepository extensionRepository;
    
    private ApplicationContext applicationContext;
    
    @Override
    public void doRegistration(Class<?> targetClz) {
        ExtensionPointI extension = (ExtensionPointI) applicationContext.getBean(targetClz);
        Extension extensionAnn = targetClz.getDeclaredAnnotation(Extension.class);
        String extPtClassName = calculateExtensionPoint(targetClz);
        ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(extPtClassName, extensionAnn.bizCode());
        ExtensionPointI preVal = extensionRepository.getExtensionRepo().put(extensionCoordinate, extension);
        if (preVal != null) {
            throw new ColaException("Duplicate registration is not allowed for :" + extensionCoordinate);
        }
    }

    /**
     * @param targetClz
     * @return
     */
    private String calculateExtensionPoint(Class<?> targetClz) {
        Class[] interfaces = targetClz.getInterfaces();
        if (ArrayUtils.isEmpty(interfaces))
            throw new ColaException("Please assign a extension point interface for "+targetClz);
        for (Class intf : interfaces) {
            String extensionPoint = intf.getSimpleName();
            if (StringUtils.contains(extensionPoint, ColaConstant.EXTENSION_EXTPT_NAMING))
                return intf.getName();
        }
        throw new ColaException("Your name of ExtensionPoint for "+targetClz+" is not valid, must be end of "+ ColaConstant.EXTENSION_EXTPT_NAMING);
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}