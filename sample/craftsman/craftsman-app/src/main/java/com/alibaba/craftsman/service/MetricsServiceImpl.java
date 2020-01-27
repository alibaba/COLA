package com.alibaba.craftsman.service;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.ExecutorBusI;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.*;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MetricsServiceImpl
 *
 * @author Frank Zhang
 * @date 2019-03-01 11:41 AM
 */
@Service
public class MetricsServiceImpl implements MetricsServiceI{

    @Autowired
    private ExecutorBusI executorBus;

    @Override
    public Response addATAMetric(ATAMetricAddCmd cmd) {
        return executorBus.send(cmd);
    }

    @Override
    public Response addSharingMetric(SharingMetricAddCmd cmd) {
        return executorBus.send(cmd);
    }

    @Override
    public Response addPatentMetric(PatentMetricAddCmd cmd) {
        return  executorBus.send(cmd);
    }

    @Override
    public Response addPaperMetric(PaperMetricAddCmd cmd) {
        return  executorBus.send(cmd);
    }

    @Override
    public Response addRefactoringMetric(RefactoringMetricAddCmd cmd) {
        return  executorBus.send(cmd);
    }

    @Override
    public Response addMiscMetric(MiscMetricAddCmd cmd) {
        return  executorBus.send(cmd);
    }

    @Override
    public Response addCodeReviewMetric(CodeReviewMetricAddCmd cmd) {
        return executorBus.send(cmd);
    }

    @Override
    public Response deleteMetric(MetricDeleteCmd cmd) {
        return executorBus.send(cmd);
    }

    @Override
    public MultiResponse<ATAMetricCO> listATAMetrics(ATAMetricQry ataMetricQry) {
        return (MultiResponse<ATAMetricCO>)executorBus.send(ataMetricQry);
    }
}
