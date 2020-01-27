package com.alibaba.craftsman.command.query;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.domain.metrics.SubMetricType;
import com.alibaba.craftsman.dto.ATAMetricQry;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Executor
public class ATAMetricQryExe implements ExecutorI<Response, ATAMetricQry> {

    @Autowired
    private MetricTunnel metricTunnel;

    @Override
    public MultiResponse<ATAMetricCO> execute(ATAMetricQry cmd) {
        List<MetricDO> metricDOList = metricTunnel.listBySubMetric(cmd.getOwnerId(), SubMetricType.ATA.getMetricSubTypeCode());
        List<ATAMetricCO> ataMetricCOList = new ArrayList<>();
        metricDOList.forEach(metricDO -> {
            ATAMetricCO ataMetricCO = JSON.parseObject(metricDO.getMetricItem(), ATAMetricCO.class);
            ataMetricCO.setOwnerId(metricDO.getUserId());
            ataMetricCOList.add(ataMetricCO);
        });
        return MultiResponse.ofWithoutTotal(ataMetricCOList);
    }

}
