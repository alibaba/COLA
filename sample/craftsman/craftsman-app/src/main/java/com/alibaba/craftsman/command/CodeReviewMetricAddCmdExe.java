package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.repository.MetricRepository;
import com.alibaba.craftsman.domain.metrics.techcontribution.CodeReviewMetric;
import com.alibaba.craftsman.domain.metrics.techcontribution.CodeReviewMetricItem;
import com.alibaba.craftsman.domain.metrics.techcontribution.ContributionMetric;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.CodeReviewMetricAddCmd;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CodeReviewMetricAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-03-04 11:14 AM
 */
@Executor
public class CodeReviewMetricAddCmdExe implements ExecutorI<Response, CodeReviewMetricAddCmd> {

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Response execute(CodeReviewMetricAddCmd cmd) {
        CodeReviewMetricItem codeReviewMetricItem = new CodeReviewMetricItem();
        BeanUtils.copyProperties(cmd, codeReviewMetricItem);
        codeReviewMetricItem.setSubMetric(new CodeReviewMetric(new ContributionMetric(new UserProfile(cmd.getOwnerId()))));
        metricRepository.save(codeReviewMetricItem);
        return Response.buildSuccess();
    }
}