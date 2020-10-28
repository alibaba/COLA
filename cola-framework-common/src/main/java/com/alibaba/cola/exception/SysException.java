package com.alibaba.cola.exception;


/**
 * System Exception is unexpected Exception, retry might work again
 *
 * @author Danny.Lee 2018/1/27
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    public SysException(String errMessage){
        super(errMessage);
    }

    public SysException(ErrorCodeI errCode, String errMessage) {
        super(errMessage);
        this.setErrCode(errCode);
    }

    public SysException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public SysException(String errMessage, ErrorCodeI errorCode, Throwable e) {
        super(errMessage, e);
        this.setErrCode(errorCode);
    }
}
