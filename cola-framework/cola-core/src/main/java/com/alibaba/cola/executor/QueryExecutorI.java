package com.alibaba.cola.executor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;

public interface QueryExecutorI<R extends Response, C extends Executor> extends ExecutorI<R,C> {

}
