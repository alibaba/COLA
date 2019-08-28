package com.alibaba.cola.dto;

/**
 * Response with single record to return
 * <p/>
 * Created by Danny.Lee on 2017/11/1.
 */

public class SingleResponse<T> extends Response {

    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setData(data);
        return singleResponse;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }

    public static <T> SingleResponse<T> buildFailure(String errCode, String errMessage) {
        SingleResponse<T> response = new SingleResponse<T>();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> SingleResponse<T> buildSuccess(){
        SingleResponse<T> response = new SingleResponse<T>();
        response.setSuccess(true);
        return response;
    }
    
}
