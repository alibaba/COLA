package com.alibaba.cola.test.customer;

import com.alibaba.cola.exception.ErrorCodeI;

/**
 * ErrorCode
 *
 * @author Frank Zhang
 * @date 2019-01-03 11:24 AM
 */
public enum ErrorCode implements ErrorCodeI{

    B_CUSTOMER_advNotAllowed("B_CUSTOMER_advNotAllowed", "不允许添加广告客户"),
    B_CUSTOMER_vipNeedApproval("B_CUSTOMER_vipNeedApproval", "VIP客户需要审批");

    private String errCode;
    private String errDesc;

    private ErrorCode(String errCode, String errDesc){
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
