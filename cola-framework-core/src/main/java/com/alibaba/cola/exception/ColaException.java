package com.alibaba.cola.exception;

/**
 * 
 * Infrastructure Exception
 * 
 * @author fulan.zjf 2017年10月22日 下午5:56:57
 */
public class ColaException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public ColaException(String errMessage){
        super(errMessage);
        this.setErrCode(ColaErrorCode.COLA_ERROR);
    }
    
    public ColaException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(ColaErrorCode.COLA_ERROR);
    }
}