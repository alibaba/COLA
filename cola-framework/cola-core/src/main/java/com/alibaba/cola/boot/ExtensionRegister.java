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
import com.alibaba.cola.exception.framework.ColaException;
import com.alibaba.cola.extension.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ExtensionRegister 
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionRegister implements RegisterI{

    @Autowired
    private ExtensionRepository extensionRepository;
    

    @Override
    public void doRegistration(Class<?> targetClz) {
        ExtensionPointI extension = (ExtensionPointI) ApplicationContextHelper.getBean(targetClz);
        Extension extensionAnn = targetClz.getDeclaredAnnotation(Extension.class);
        String extPtClassName = calculateExtensionPoint(targetClz);
        BizScenario bizScenario = BizScenario.valueOf(extensionAnn.bizId(), extensionAnn.useCase(), extensionAnn.scenario());
        ExtensionCoordinate extensionCoordinate = new ExtensionCoordinate(extPtClassName, bizScenario.getUniqueIdentity());
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

}