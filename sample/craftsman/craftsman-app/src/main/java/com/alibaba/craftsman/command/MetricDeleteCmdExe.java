package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MetricTunnel metricTunnel;

    public Response execute(MetricDeleteCmd cmd) {

        metricTunnel.delete(cmd.getMetricId(), cmd.getOperater());

        return Response.buildSuccess();
    }
}