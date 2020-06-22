package com.alibaba.craftsman.command.query;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.craftsman.domain.metrics.SubMetricType;
import com.alibaba.craftsman.dto.ATAMetricQry;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ATAMetricQryExe{

    @Resource
    private MetricTunnel metricTunnel;

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
