/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.assembler;


/**
 * 适用于消息，查询，HSF等接口的参数装配
 * Assembler Interface
 *
 * @author fulan.zjf 2017-11-07
 */
public interface AssemblerI<F, T> {

    default T assemble(F from) {
        return null;
    }

    default void assemble(F from, T to) {
    }
}
