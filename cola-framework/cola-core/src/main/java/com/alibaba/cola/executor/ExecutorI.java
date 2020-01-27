package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;

/**
 * 
 * CommandExecutorI
 * 
 * @author fulan.zjf 2017年10月21日 下午11:01:05
 */
public interface ExecutorI<R extends Response, C extends Executor> {

    public R execute(C cmd);
}
