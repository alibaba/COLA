package com.alibaba.cola.exception;

/**
 * BizException is known Exception, no need retry
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage){
        super(errMessage);
    }

    public BizException(ErrorCodeI errCode, String errMessage){
    	super(errMessage);
    	this.setErrCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
}