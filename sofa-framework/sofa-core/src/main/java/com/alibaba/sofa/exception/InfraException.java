package com.alibaba.sofa.exception;

/**
 * 
 * Infrastructure Exception
 * 
 * @author fulan.zjf 2017年10月22日 下午5:56:57
 */
public class InfraException extends CrmException{
    
    private static final long serialVersionUID = 1L;
    
    public InfraException(String errMessage){
        super(errMessage);
        this.setErrCode(BasicErrorCode.INFRA_ERROR);
    }
    
    public InfraException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.setErrCode(BasicErrorCode.INFRA_ERROR);
    }
}