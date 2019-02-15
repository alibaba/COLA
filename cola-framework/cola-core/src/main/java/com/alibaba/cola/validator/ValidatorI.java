/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.validator;

/**
 * Validator Interface
 * @author fulan.zjf 2017-11-04
 */
public interface ValidatorI<T> {
    
    /**
     * Validate candidate, throw according exception if failed
     * @param candidate
     */
    public void validate(T candidate);

}
