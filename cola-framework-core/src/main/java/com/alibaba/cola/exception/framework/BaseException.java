package com.alibaba.cola.exception.framework;

import com.alibaba.cola.dto.ErrorCodeI;

/**
 * 
 * Base Exception is the parent of all exceptions
 * 
 * @author fulan.zjf 2017年10月22日 上午12:00:39
 */
public abstract class BaseException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    private ErrorCodeI errCode;
    
    public BaseException(String errMessage){
        super(errMessage);
    }
    
    public BaseException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
    
    public ErrorCodeI getErrCode() {
        return errCode;
    }
    
    public void setErrCode(ErrorCodeI errCode) {
        this.errCode = errCode;
    }
    
}
