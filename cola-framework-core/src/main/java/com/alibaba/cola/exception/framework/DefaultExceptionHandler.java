package com.alibaba.cola.exception.framework;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.ErrorCodeI;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.ExceptionHandlerI;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;

/**
 * DefaultExceptionHandler
 *
 * @author Frank Zhang
 * @date 2019-01-08 9:51 AM
 */
public class DefaultExceptionHandler implements ExceptionHandlerI {

    private Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    public static DefaultExceptionHandler singleton = new DefaultExceptionHandler();

    @Override
    public void handleException(Command cmd, Response response, Exception exception) {
        buildResponse(response, exception);
        printLog(cmd, response, exception);
    }

    private void printLog(Command cmd, Response response, Exception exception) {
        if(exception instanceof BaseException){
            //biz exception is expected, only warn it
            logger.warn(buildErrorMsg(cmd, response));
        }
        else{
            //sys exception should be monitored, and pay attention to it
            logger.error(buildErrorMsg(cmd, response), exception);
        }
    }

    private String buildErrorMsg(Command cmd, Response response) {
        return "Process [" + cmd + "] failed, errorCode: "
                + response.getErrCode() + " errorMsg:"
                + response.getErrMessage();
    }

    private void buildResponse(Response response, Exception exception) {
        if (exception instanceof BaseException) {
            ErrorCodeI errCode = ((BaseException) exception).getErrCode();
            response.setErrCode(errCode.getErrCode());
        }
        else {
            response.setErrCode(BasicErrorCode.SYS_ERROR.getErrCode());
        }
        response.setErrMessage(exception.getMessage());
        response.setSuccess(false);
    }
}
