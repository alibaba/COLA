package com.alibaba.craftsman.service;

import com.alibaba.cola.command.CommandBusI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.command.*;
import com.alibaba.craftsman.dto.*;
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
    private CommandBusI commandBus;

    @Override
    public Response addATAMetric(ATAMetricAddCmd cmd) {
        return commandBus.send(cmd, ATAMetricAddCmdExe.class);
    }

    @Override
    public Response addSharingMetric(SharingMetricAddCmd cmd) {
        return commandBus.send(cmd, SharingMetricAddCmdExe.class);
    }

    @Override
    public Response addPatentMetric(PatentMetricAddCmd cmd) {
        return  commandBus.send(cmd, PatentMetricAddCmdExe.class);
    }

    @Override
    public Response addPaperMetric(PaperMetricAddCmd cmd) {
        return  commandBus.send(cmd, PaperMetricAddCmdExe.class);
    }

    @Override
    public Response addRefactoringMetric(RefactoringMetricAddCmd cmd) {
        return  commandBus.send(cmd, RefactoringMetricAddCmdExe.class);
    }

    @Override
    public Response addMiscMetric(MiscMetricAddCmd cmd) {
        return  commandBus.send(cmd, MiscMetricAddCmdExe.class);
    }

    @Override
    public Response addCodeReviewMetric(CodeReviewMetricAddCmd cmd) {
        return commandBus.send(cmd, CodeReviewMetricAddCmdExe.class);
    }

    @Override
    public Response deleteMetric(MetricDeleteCmd cmd) {
        return commandBus.send(cmd, MetricDeleteCmdExe.class);
    }
}
