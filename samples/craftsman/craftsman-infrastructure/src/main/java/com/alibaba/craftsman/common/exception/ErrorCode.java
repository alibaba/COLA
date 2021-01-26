package com.alibaba.craftsman.common.exception;

/**
 * ErrorCode
 *
 * @author Frank Zhang
 * @date 2019-01-04 11:00 AM
 */
public enum ErrorCode {

    B_CUSTOMER_companyNameConflict("B_CUSTOMER_companyNameConflict", "客户公司名冲突");

    private final String errCode;
    private final String errDesc;

    private ErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public static ErrorCode statOf(String ecode) {
        for (ErrorCode errorCode : values()){
            if (errorCode.getErrCode().equals(ecode))
                return errorCode;
        }
        return null;
    }
}
