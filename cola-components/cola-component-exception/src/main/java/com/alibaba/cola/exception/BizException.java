package com.alibaba.cola.exception;

/**
 * BizException is known Exception, no need retry
 *
 * @author Frank Zhang
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ERR_CODE = "BIZ_ERROR";

    public BizException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public BizException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public BizException(String errMessage, Throwable e) {
        super(DEFAULT_ERR_CODE, errMessage, e);
    }

    public BizException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}