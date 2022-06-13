package com.huawei.charging.domain;

public class BizException extends RuntimeException{

    public BizException(String errMessage) {
        super(errMessage);
    }

    public static BizException of(String errMessage){
        return new BizException(errMessage);
    }
}
