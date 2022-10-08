package com.alibaba.cola.extension;

import lombok.Data;

/**
 * @author huyuanxin
 */
@Data
public class ExecuteException extends RuntimeException{

    private final Throwable cause;

    public ExecuteException(Throwable cause) {
        this.cause = cause;
    }

}
