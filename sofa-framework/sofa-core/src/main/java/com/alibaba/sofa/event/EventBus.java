package com.alibaba.sofa.event;

import com.alibaba.sofa.dto.Response;
import com.alibaba.sofa.dto.event.Event;
import com.alibaba.sofa.exception.BasicErrorCode;
import com.alibaba.sofa.exception.CrmException;
import com.alibaba.sofa.exception.ErrorCodeI;
import com.alibaba.sofa.exception.InfraException;
import com.alibaba.sofa.logger.Logger;
import com.alibaba.sofa.logger.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event Bus
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventBus implements EventBusI{
    Logger logger = LoggerFactory.getLogger(EventBus.class);

    @Autowired
    private EventHub eventHub;

    @SuppressWarnings("unchecked")
    @Override
    public Response fire(Event event) {
        Response response = null;
        try {
            response = eventHub.getEventHandler(event.getClass()).execute(event);
        }catch (Exception exception) {
            response = handleException(event, response, exception);
        }
        return response;
    }

    private Response handleException(Event cmd, Response response, Exception exception) {
        Class responseClz = eventHub.getResponseRepository().get(cmd.getClass());
        try {
            response = (Response) responseClz.newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InfraException(e.getMessage());
        }
        if (exception instanceof CrmException) {
            ErrorCodeI errCode = ((CrmException) exception).getErrCode();
            response.setErrCode(errCode.getErrCode());
        }
        else {
            response.setErrCode(BasicErrorCode.SYS_ERROR.getErrCode());
        }
        response.setErrMessage(exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return response;
    }
}
