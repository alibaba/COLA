/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.extension;

import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.context.Context;
import com.alibaba.cola.exception.ColaException;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ExtensionExecutor 
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionExecutor extends AbstractExecutorFacade {

    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    @Autowired
    private ExtensionRepository extensionRepository;

    @Override
    protected <C> C locateComponent(Class<C> targetClz, Context context) {
        C extension = locateExtension(targetClz, context);
        logger.debug("[Located Extension]: "+extension.getClass().getSimpleName());
        return extension;
    }

    /**
     * if the bizCode is "ali.tmall.supermarket"
     *
     * the search path is as below:
     * 1、first try to get extension by "ali.tmall.supermarket", if get, return it.
     * 2、loop try to get extension by "ali.tmall", if get, return it.
     * 3、loop try to get extension by "ali", if get, return it.
     * 4、if not found, try the default extension
     * @param targetClz
     */
    protected <Ext> Ext locateExtension(Class<Ext> targetClz, Context context) {
        Ext extension;
        checkNull(context);
        String bizCode =  context.getBizCode();
        logger.debug("Biz Code in locateExtension is : " + bizCode);

        // first try
        extension = firstTry(targetClz, bizCode);
        if (extension != null) {
            return extension;
        }

        // loop try
        extension = loopTry(targetClz, bizCode);
        if (extension != null) {
            return extension;
        }

        // last try
        extension = tryDefault(targetClz);
        if (extension != null) {
            return extension;
        }

        throw new ColaException("Can not find extension with ExtensionPoint: "+targetClz+" BizCode:"+bizCode);
    }

    private  <Ext> Ext firstTry(Class<Ext> targetClz, String bizCode) {
        return (Ext)extensionRepository.getExtensionRepo().get(new ExtensionCoordinate(targetClz.getName(), bizCode));
    }

    private <Ext> Ext loopTry(Class<Ext> targetClz, String bizCode){
        Ext extension;
        if (bizCode == null){
            return null;
        }
        int lastDotIndex = bizCode.lastIndexOf(ColaConstant.BIZ_CODE_SEPARATOR);
        while(lastDotIndex != -1){
            bizCode = bizCode.substring(0, lastDotIndex);
            extension =(Ext)extensionRepository.getExtensionRepo().get(new ExtensionCoordinate(targetClz.getName(), bizCode));
            if (extension != null) {
                return extension;
            }
            lastDotIndex = bizCode.lastIndexOf(ColaConstant.BIZ_CODE_SEPARATOR);
        }
        return null;
    }

    private <Ext> Ext tryDefault(Class<Ext> targetClz) {
        return (Ext)extensionRepository.getExtensionRepo().get(new ExtensionCoordinate(targetClz.getName(), ColaConstant.DEFAULT_BIZ_CODE));
    }


    private void checkNull(Context context){
        if(context == null){
            throw new ColaException("Context can not be null for extension");
        }
    }

}
