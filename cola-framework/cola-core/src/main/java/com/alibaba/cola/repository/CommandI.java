package com.alibaba.cola.repository;

import com.alibaba.cola.common.ColaConstant;

/**
 * @author lorne
 */
public interface CommandI<R> {

    /**
     * 执行RepositoryHandler的method方法，
     * 方法的执行必须入参是CommandI对象
     * @return method name
     */
    default String command() {
        return ColaConstant.COMMAND_METHOD;
    }

}
