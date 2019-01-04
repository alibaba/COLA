package com.alibaba.cola.exception;

/**
 * 
 * COLA framework Exception
 * 
 * @author fulan.zjf 2017年10月22日 下午5:56:57
 */
public class ColaException extends AppException {
    
    private static final long serialVersionUID = 1L;
    
    public ColaException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.S_COLA_ERROR);
    }
    
    public ColaException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.S_COLA_ERROR);
    }
}