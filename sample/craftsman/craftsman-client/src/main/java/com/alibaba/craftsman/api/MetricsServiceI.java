package com.alibaba.craftsman.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.dto.*;

/**
 * MetricsServiceI
 *
 * @author Frank Zhang
 * @date 2019-03-01 10:06 AM
 */
public interface MetricsServiceI {
    public Response addATAMetric(ATAMetricAddCmd cmd);
    public Response addSharingMetric(SharingMetricAddCmd cmd);
    public Response addPatentMetric(PatentMetricAddCmd cmd);
    public Response addPaperMetric(PaperMetricAddCmd cmd);
    public Response addRefactoringMetric(RefactoringMetricAddCmd cmd);
    public Response addMiscMetric(MiscMetricAddCmd cmd);
    public Response addCodeReviewMetric(CodeReviewMetricAddCmd cmd);
    public Response deleteMetric(MetricDeleteCmd cmd);
}
