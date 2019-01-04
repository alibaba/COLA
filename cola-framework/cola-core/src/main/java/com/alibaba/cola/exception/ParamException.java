package com.alibaba.cola.exception;

public class ParamException extends AppException {
    
    private static final long serialVersionUID = 1L;
    
    public ParamException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.P_COMMON_ERROR);
    }

    public ParamException(ErrorCodeI errCode, String errMessage){
        super(errMessage);
        this.setErrCode(errCode);
    }

    public ParamException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.P_COMMON_ERROR);
    }
}
