package com.alibaba.craftsman.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.dto.*;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;

/**
 * MetricsServiceI
 *
 * @author Frank Zhang
 * @date 2019-03-01 10:06 AM
 */
public interface MetricsServiceI {
    Response addATAMetric(ATAMetricAddCmd cmd);
    Response addSharingMetric(SharingMetricAddCmd cmd);
    Response addPatentMetric(PatentMetricAddCmd cmd);
    Response addPaperMetric(PaperMetricAddCmd cmd);
    Response addRefactoringMetric(RefactoringMetricAddCmd cmd);
    Response addMiscMetric(MiscMetricAddCmd cmd);
    Response addCodeReviewMetric(CodeReviewMetricAddCmd cmd);
    Response deleteMetric(MetricDeleteCmd cmd);
    MultiResponse<ATAMetricCO> listATAMetrics(ATAMetricQry ataMetricQry);
}
