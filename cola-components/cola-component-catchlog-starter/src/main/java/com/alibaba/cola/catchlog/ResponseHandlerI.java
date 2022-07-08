package com.alibaba.cola.catchlog;

import com.alibaba.cola.exception.BaseException;

public interface ResponseHandlerI {
    public Object handle(Class returnType, String errCode, String errMsg);
}
