package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.domain.gateway.MetricGateway;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MetricDeleteCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-04 3:01 PM
 */
@Component
public class MetricDeleteCmdExe{

    @Resource
    private MetricGateway metricGateway;

    public Response execute(MetricDeleteCmd cmd) {

        metricGateway.delete(cmd.getMetricId(), cmd.getOperater());

        return Response.buildSuccess();
    }
}
