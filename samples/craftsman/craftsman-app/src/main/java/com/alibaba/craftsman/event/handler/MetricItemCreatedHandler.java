package com.alibaba.craftsman.event.handler;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.api.UserProfileServiceI;
import com.alibaba.craftsman.dto.RefreshScoreCmd;
import com.alibaba.craftsman.dto.domainevent.MetricItemCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

public class MetricItemCreatedHandler {

    private Logger logger = LoggerFactory.getLogger(MetricItemCreatedHandler.class);

    @Autowired
    private UserProfileServiceI userProfileService;

    public Response execute(MetricItemCreatedEvent event) {
        logger.debug("Handling Event: " + event);
        RefreshScoreCmd cmd = new RefreshScoreCmd(event.getUserId());
        userProfileService.refreshScore(cmd);
        return Response.buildSuccess();
    }
}
