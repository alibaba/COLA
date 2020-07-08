/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.extension;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ExtensionRepository 
 * @author fulan.zjf 2017-11-05
 */
@Component
public class ExtensionRepository {

    public Map<ExtensionCoordinate, ExtensionPointI> getExtensionRepo() {
        return extensionRepo;
    }

    private Map<ExtensionCoordinate, ExtensionPointI> extensionRepo = new HashMap<>();


}
