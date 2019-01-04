package com.alibaba.cola.exception;

public class BizException extends AppException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.B_COMMON_ERROR);
    }

    public BizException(ErrorCodeI errCode, String errMessage){
    	super(errMessage);
    	this.setErrCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.B_COMMON_ERROR);
    }
}