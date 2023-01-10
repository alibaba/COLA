/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.extension;

import com.alibaba.cola.extension.register.AbstractComponentExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * ExtensionExecutor
 *
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionExecutor extends AbstractComponentExecutor {

    private static final String EXTENSION_NOT_FOUND = "extension_not_found";

    private Logger logger = LoggerFactory.getLogger(ExtensionExecutor.class);

    @Resource
    private ExtensionRepository extensionRepository;

    @Override
    protected <C> C locateComponent(Class<C> targetClz, BizScenario bizScenario) {
        C extension = locateExtension(targetClz, bizScenario);
        logger.debug("[Located Extension]: " + extension.getClass().getSimpleName());
        return extension;
    }

    /**
     * if the bizScenarioUniqueIdentity is "ali.tmall.supermarket"
     * <p>
     * the search path is as below:
     * 1、first try to get extension by "ali.tmall.supermarket", if get, return it.
     * 2、loop try to get extension by "ali.tmall", if get, return it.
     * 3、loop try to get extension by "ali", if get, return it.
     * 4、if not found, try the default extension
     *
     * @param targetClz
     */
    protected <Ext> Ext locateExtension(Class<Ext> targetClz, BizScenario bizScenario) {
        checkNull(bizScenario);

        Ext extension;

        logger.debug("BizScenario in locateExtension is : " + bizScenario.getUniqueIdentity());

        // first try with full namespace
        extension = firstTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // second try with default scenario
        extension = secondTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        String errMessage = "Can not find extension with ExtensionPoint: " +
                targetClz + " BizScenario:" + bizScenario.getUniqueIdentity();
        throw new ExtensionException(EXTENSION_NOT_FOUND, errMessage);
    }

    /**
     * first try with full namespace
     * <p>
     * example:  biz1.useCase1.scenario1
     */
    private <Ext> Ext firstTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("First trying with " + bizScenario.getUniqueIdentity());
        return locate(targetClz.getName(), bizScenario.getUniqueIdentity());
    }

    /**
     * second try with default scenario
     * <p>
     * example:  biz1.useCase1.#defaultScenario#
     */
    private <Ext> Ext secondTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("Second trying with " + bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
    }

    /**
     * third try with default use case + default scenario
     * <p>
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private <Ext> Ext defaultUseCaseTry(Class<Ext> targetClz, BizScenario bizScenario) {
        logger.debug("Third trying with " + bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
    }

    private <Ext> Ext locate(String name, String uniqueIdentity) {
        final Ext ext = (Ext) extensionRepository.getExtensionRepo().
                get(new ExtensionCoordinate(name, uniqueIdentity));
        return ext;
    }

    private void checkNull(BizScenario bizScenario) {
        if (bizScenario == null) {
            throw new IllegalArgumentException("BizScenario can not be null for extension");
        }
    }

}
