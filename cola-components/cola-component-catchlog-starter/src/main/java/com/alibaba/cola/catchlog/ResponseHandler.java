package com.alibaba.cola.catchlog;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * ResponseHandler
 *
 * @author Frank Zhang
 * @date 2020-11-10 3:18 PM
 */
@Slf4j
public class ResponseHandler {

    public static Object handle(Class returnType, String errCode, String errMsg){
        if (isColaResponse(returnType)){
            return handleColaResponse(returnType, errCode, errMsg);
        }
        return null;
    }

    public static Object handle(Class returnType, BaseException e){
        return handle(returnType, e.getErrCode(), e.getMessage());
    }



    private static Object handleColaResponse(Class returnType, String errCode, String errMsg) {
        try {
            Response response = (Response)returnType.newInstance();
            response.setSuccess(false);
            response.setErrCode(errCode);
            response.setErrMessage(errMsg);
            return response;
        }
        catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return  null;
        }
    }


    private static boolean isColaResponse(Class returnType) {
        return  returnType == Response.class || returnType.getGenericSuperclass() == Response.class;
    }
}
