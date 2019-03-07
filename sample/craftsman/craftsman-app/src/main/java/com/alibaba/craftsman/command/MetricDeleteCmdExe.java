package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MetricDeleteCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-04 3:01 PM
 */
@Command
public class MetricDeleteCmdExe implements CommandExecutorI<Response, MetricDeleteCmd> {

    @Autowired
    private MetricTunnel metricTunnel;

    @Override
    public Response execute(MetricDeleteCmd cmd) {

        metricTunnel.delete(cmd.getMetricId(), cmd.getOperater());

        return Response.buildSuccess();
    }
}