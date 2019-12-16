package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.repository.MetricRepository;
import com.alibaba.craftsman.domain.metrics.techinfluence.InfluenceMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.PaperMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.PaperMetricItem;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.PaperMetricAddCmd;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PaperMetricAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:41 AM
 */
@Command
public class PaperMetricAddCmdExe implements CommandExecutorI<Response, PaperMetricAddCmd> {

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Response execute(PaperMetricAddCmd cmd) {
        PaperMetricItem paperMetricItem = new PaperMetricItem();
        BeanUtils.copyProperties(cmd.getPaperMetricCO(), paperMetricItem);
        paperMetricItem.setSubMetric(new PaperMetric(new InfluenceMetric(new UserProfile(cmd.getPaperMetricCO().getOwnerId()))));
        paperMetricItem.setMetricOwner(new UserProfile(cmd.getPaperMetricCO().getOwnerId()));
        metricRepository.save(paperMetricItem);

        return Response.buildSuccess();
    }
}