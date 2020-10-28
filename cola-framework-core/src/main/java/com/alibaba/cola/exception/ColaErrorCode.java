package com.alibaba.cola.exception;

import com.alibaba.cola.exception.ErrorCodeI;

/**
 *
 * There are only 3 basic ErrorCode:
 * COLA_ERROR
 *
 * Created by fulan.zjf on 2017/12/18.
 */
public enum ColaErrorCode implements ErrorCodeI {

    COLA_ERROR("COLA_FRAMEWORK_ERROR" , "COLA框架错误");

    private String errCode;
    private String errDesc;

    private ColaErrorCode(String errCode, String errDesc){
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
