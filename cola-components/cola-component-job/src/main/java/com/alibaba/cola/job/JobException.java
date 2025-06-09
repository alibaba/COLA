package com.alibaba.cola.job;

public class JobException extends RuntimeException{
    public JobException(String message){
        super(message);
    }

    public JobException(Exception e){
        super(e);
    }
}

