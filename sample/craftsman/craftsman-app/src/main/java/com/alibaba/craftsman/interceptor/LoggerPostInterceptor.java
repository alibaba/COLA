package com.alibaba.craftsman.interceptor;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.ExecutorInterceptorI;
import com.alibaba.cola.executor.PostInterceptor;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;

@PostInterceptor
public class LoggerPostInterceptor implements ExecutorInterceptorI {

    Logger logger = LoggerFactory.getLogger(LoggerPostInterceptor.class);

    @Override
    public void postIntercept(Executor executor, Response response) {
        logger.debug("End processing "+ executor.getClass()+" Response:"+response);
    }

}
