package com.alibaba.cola.exception.framework;

import com.alibaba.cola.dto.ErrorCodeI;

/**
 *
 * There are only 3 basic ErrorCode:
 * COLA_ERROR
 * BIZ_ERROR
 * SYS_ERROR
 *
 * Created by fulan.zjf on 2017/12/18.
 */
public enum BasicErrorCode implements ErrorCodeI {

    BIZ_ERROR("BIZ_ERROR" , "通用的业务逻辑错误"),

    COLA_ERROR("COLA_FRAMEWORK_ERROR" , "COLA框架错误"),

    SYS_ERROR("SYS_ERROR" , "未知的系统错误" );

    private String errCode;
    private String errDesc;

    private BasicErrorCode(String errCode, String errDesc){
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
