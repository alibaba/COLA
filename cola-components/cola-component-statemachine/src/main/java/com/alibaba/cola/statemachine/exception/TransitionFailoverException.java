package com.alibaba.cola.statemachine.exception;

/**
 * @author 龙也
 * @date 2022/9/15 12:08 PM
 */
public class TransitionFailoverException extends RuntimeException {

    public TransitionFailoverException(String errMsg) {
        super(errMsg);
    }
}
