package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MetricDeleteCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-04 3:01 PM
 */
@Executor
public class MetricDeleteCmdExe implements ExecutorI<Response, MetricDeleteCmd> {

    @Autowired
    private MetricTunnel metricTunnel;

    @Override
    public Response execute(MetricDeleteCmd cmd) {

        metricTunnel.delete(cmd.getMetricId(), cmd.getOperater());

        return Response.buildSuccess();
    }
}