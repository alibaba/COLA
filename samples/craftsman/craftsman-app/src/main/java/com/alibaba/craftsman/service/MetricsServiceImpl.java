package com.alibaba.craftsman.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.command.*;
import com.alibaba.craftsman.command.query.ATAMetricQryExe;
import com.alibaba.craftsman.dto.*;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.Validator;

/**
 * MetricsServiceImpl
 *
 * @author Frank Zhang
 * @date 2019-03-01 11:41 AM
 */
@Service
@Validated
@CatchAndLog
public class MetricsServiceImpl implements MetricsServiceI{

    @Resource
    private ATAMetricAddCmdExe ataMetricAddCmdExe;
    @Resource
    private SharingMetricAddCmdExe sharingMetricAddCmdExe;
    @Resource
    private PatentMetricAddCmdExe patentMetricAddCmdExe;
    @Resource
    private PaperMetricAddCmdExe paperMetricAddCmdExe;
    @Resource
    private RefactoringMetricAddCmdExe refactoringMetricAddCmdExe;
    @Resource
    private MiscMetricAddCmdExe miscMetricAddCmdExe;
    @Resource
    private CodeReviewMetricAddCmdExe codeReviewMetricAddCmdExe;
    @Resource
    private MetricDeleteCmdExe metricDeleteCmdExe;
    @Resource
    private ATAMetricQryExe ataMetricQryExe;
    @Resource
    Validator validator;

    @Override
    public Response addATAMetric(@Valid ATAMetricAddCmd cmd) {
        // Set<ConstraintViolation<ATAMetricAddCmd>> violations =  validator.validate(cmd);
        // if (!violations.isEmpty()) {
        //     throw new ConstraintViolationException(violations);
        // }
        return ataMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addSharingMetric(SharingMetricAddCmd cmd) {
        return sharingMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addPatentMetric(PatentMetricAddCmd cmd) {
        return  patentMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addPaperMetric(PaperMetricAddCmd cmd) {
        return  paperMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addRefactoringMetric(RefactoringMetricAddCmd cmd) {
        return  refactoringMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addMiscMetric(MiscMetricAddCmd cmd) {
        return  miscMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response addCodeReviewMetric(CodeReviewMetricAddCmd cmd) {
        return codeReviewMetricAddCmdExe.execute(cmd);
    }

    @Override
    public Response deleteMetric(MetricDeleteCmd cmd) {
        return metricDeleteCmdExe.execute(cmd);
    }

    @Override
    public MultiResponse<ATAMetricCO> listATAMetrics(ATAMetricQry ataMetricQry) {
        return ataMetricQryExe.execute(ataMetricQry);
    }
}
