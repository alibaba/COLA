package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.CodeReviewMetricAddCmd;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CodeReviewMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-04 11:23 AM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class CodeReviewMetricAddCmdExeTest extends MockTestBase {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testSuccess(){
        CodeReviewMetricAddCmd codeReviewMetricAddCmd = prepareCodeReviewMetricAddCmd("CodeReviewMetricAddCmdExeTest_098873");

        Response response = metricsService.addCodeReviewMetric(codeReviewMetricAddCmd);

        Assert.assertTrue(response.isSuccess());

    }

    public static CodeReviewMetricAddCmd prepareCodeReviewMetricAddCmd(String ownerId) {
        CodeReviewMetricAddCmd codeReviewMetricAddCmd = new CodeReviewMetricAddCmd();
        codeReviewMetricAddCmd.setOwnerId(ownerId);
        codeReviewMetricAddCmd.setReviewId("72376263");
        codeReviewMetricAddCmd.setNoteCount(8);
        codeReviewMetricAddCmd.setReviewDocLink("http://www.alibaba.com ");
        return codeReviewMetricAddCmd;
    }
}
