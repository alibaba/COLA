package com.alibaba.cola.exception;

/**
 * 
 * Application Exception
 * 
 * @author fulan.zjf 2017年10月22日 上午12:00:39
 */
public abstract class AppException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    private ErrorCodeI errCode;
    
    public AppException(String errMessage){
        super(errMessage);
    }
    
    public AppException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
    
    public ErrorCodeI getErrCode() {
        return errCode;
    }
    
    public void setErrCode(ErrorCodeI errCode) {
        this.errCode = errCode;
    }
    
}
