package com.alibaba.sofa.exception;

/**
 * Created by Danny.Lee on 2017/11/1.
 */
public class Preconditions {

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new ParamException("");
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new ParamException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new BizException("");
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new BizException(String.valueOf(errorMessage));
        }
    }
}
