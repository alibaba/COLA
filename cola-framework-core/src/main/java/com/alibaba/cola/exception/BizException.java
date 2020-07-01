package com.alibaba.cola.exception;

import com.alibaba.cola.dto.ErrorCodeI;
import com.alibaba.cola.exception.framework.BaseException;
import com.alibaba.cola.exception.framework.BasicErrorCode;

/**
 * BizException is known Exception, no need retry
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BizException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }

    public BizException(ErrorCodeI errCode, String errMessage){
    	super(errMessage);
    	this.setErrCode(errCode);
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.BIZ_ERROR);
    }
}