package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.domain.metrics.techinfluence.ATAMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.ATAMetricItem;
import com.alibaba.craftsman.domain.metrics.techinfluence.InfluenceMetric;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.ATAMetricAddCmd;
import com.alibaba.craftsman.repository.MetricRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ATAMetricAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-01 11:42 AM
 */
@Executor
public class ATAMetricAddCmdExe implements ExecutorI<Response, ATAMetricAddCmd> {

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Response execute(ATAMetricAddCmd cmd) {
        ATAMetricItem ataMetricItem = new ATAMetricItem();
        BeanUtils.copyProperties(cmd.getAtaMetricCO(), ataMetricItem);
        ataMetricItem.setSubMetric(new ATAMetric(new InfluenceMetric(new UserProfile(cmd.getAtaMetricCO().getOwnerId()))));
        metricRepository.save(ataMetricItem);
        return Response.buildSuccess();
    }
}
