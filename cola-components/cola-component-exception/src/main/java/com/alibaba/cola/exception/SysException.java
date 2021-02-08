package com.alibaba.cola.exception;

/**
 * System Exception is unexpected Exception, retry might work again
 *
 * @author Danny.Lee 2018/1/27
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ERR_CODE = "SYS_ERROR";

    public SysException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public SysException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public SysException(String errMessage, Throwable e) {
        super(DEFAULT_ERR_CODE, errMessage, e);
    }

    public SysException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}
