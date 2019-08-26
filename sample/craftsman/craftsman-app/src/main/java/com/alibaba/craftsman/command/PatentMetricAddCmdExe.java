package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.repository.MetricRepository;
import com.alibaba.craftsman.domain.metrics.techinfluence.*;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.PatentMetricAddCmd;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PatentMetricAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:41 AM
 */
@Command
public class PatentMetricAddCmdExe implements CommandExecutorI<Response, PatentMetricAddCmd> {

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Response execute(PatentMetricAddCmd cmd) {
        PatentMetricItem patentMetricItem = new PatentMetricItem();
        BeanUtils.copyProperties(cmd.getPatentMetricCO(), patentMetricItem);
        patentMetricItem.setSubMetric(new PatentMetric(new InfluenceMetric(new UserProfile(cmd.getPatentMetricCO().getOwnerId()))));
        patentMetricItem.setAuthorType(AuthorType.valueOf(cmd.getPatentMetricCO().getAuthorType()));
        metricRepository.save(patentMetricItem);
        return Response.buildSuccess();
    }
}