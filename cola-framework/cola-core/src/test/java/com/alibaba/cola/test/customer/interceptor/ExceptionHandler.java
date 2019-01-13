package com.alibaba.cola.test.customer.interceptor;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.*;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ExceptionHandler
 *
 * @author Frank Zhang
 * @date 2019-01-03 12:05 AM
 */
@Component
public class ExceptionHandler implements ExceptionHandlerI{
    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public void handleException(Command cmd, Response response, Exception exception) {
        assembleResponse(response, exception);
        printLog(cmd, response, exception);
    }

    private void assembleResponse(Response response, Exception exception) {
        if (exception instanceof BaseException) {
            ErrorCodeI errCode = ((BaseException) exception).getErrCode();
            response.setErrCode(errCode.getErrCode());
        }
        else {
            response.setErrCode(BasicErrorCode.S_UNKNOWN.getErrCode());
        }
        response.setErrMessage(exception.getMessage());
    }

    private void printLog(Command cmd, Response response, Exception exception) {
        if(exception instanceof BizException){
            logger.warn(formErrorMessage(cmd, response), exception);
        }
        else{
            logger.error(formErrorMessage(cmd, response), exception);
        }
    }

    private String formErrorMessage(Command cmd, Response response){
        return "Execute ["+cmd+"] error, errorCode: "+ response.getErrCode() + " errorMsg:"+ response.getErrMessage();
    }
}
