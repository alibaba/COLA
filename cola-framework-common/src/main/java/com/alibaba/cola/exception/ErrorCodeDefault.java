package com.alibaba.cola.exception;

/**
 * ErrorCodeDefault
 *
 * @author Frank Zhang
 * @date 2020-10-27 3:25 PM
 */
public enum  ErrorCodeDefault implements ErrorCodeI {

    BIZ_ERROR("BIZ_ERROR" , "通用的业务逻辑错误"),

    SYS_ERROR("SYS_ERROR" , "未知的系统错误" );

    private String errCode;
    private String errDesc;

    private ErrorCodeDefault(String errCode, String errDesc){
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}
