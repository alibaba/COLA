/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.sofa.boot;

import java.lang.annotation.Annotation;

/**
 * Register Interface 
 * @author fulan.zjf 2017-11-04
 */
public interface RegisterI {
    public void doRegistration(Class<?> targetClz);
}
