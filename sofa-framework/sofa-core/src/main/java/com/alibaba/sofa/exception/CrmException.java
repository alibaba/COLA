package com.alibaba.sofa.exception;

/**
 * 
 * CRM Exception
 * 
 * @author fulan.zjf 2017年10月22日 上午12:00:39
 */
public abstract class CrmException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    private ErrorCodeI errCode;
    
    public CrmException(String errMessage){
        super(errMessage);
    }
    
    public CrmException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
    
    public ErrorCodeI getErrCode() {
        return errCode;
    }
    
    public void setErrCode(ErrorCodeI errCode) {
        this.errCode = errCode;
    }
    
}
