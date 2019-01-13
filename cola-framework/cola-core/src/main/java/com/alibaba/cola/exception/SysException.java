package com.alibaba.cola.exception;

/**
 * SysException
 *
 * @author Frank Zhang
 * @date 2018-12-29 4:38 PM
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 1L;

    public SysException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.S_UNKNOWN);
    }

    public SysException(ErrorCodeI errCode, String errMessage){
        super(errMessage);
        this.setErrCode(errCode);
    }

    public SysException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.S_UNKNOWN);
    }
}
