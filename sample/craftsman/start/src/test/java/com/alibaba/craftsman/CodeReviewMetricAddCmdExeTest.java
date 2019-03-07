package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.CodeReviewMetricAddCmd;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * CodeReviewMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-04 11:23 AM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CodeReviewMetricAddCmdExeTest {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
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
