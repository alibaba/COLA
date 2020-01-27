package com.alibaba.cola.exception;

import com.alibaba.cola.dto.Executor;
import com.alibaba.cola.dto.Response;

/**
 * ExceptionHandlerI provide a backdoor that Application can override the default Exception handling
 *
 * @author Frank Zhang
 * @date 2019-01-02 11:25 PM
 */
public interface ExceptionHandlerI {
    public void handleException(Executor cmd, Response response, Exception exception);
}
