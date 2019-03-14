package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.MiscMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.MiscMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MiscMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-04 1:47 PM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class MiscMetricAddCmdExeTest extends MockTestBase {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testSuccess(){
        MiscMetricAddCmd miscMetricAddCmd = new MiscMetricAddCmd();
        MiscMetricCO miscMetricCO = new MiscMetricCO();
        miscMetricCO.setOwnerId("MiscMetricAddCmdExeTest_09888");
        miscMetricCO.setName("Tech highlight");
        miscMetricCO.setContent("Highlight content");
        miscMetricCO.setDocUrl("docUrl");
        miscMetricCO.setCodeUrl("codeUrl");
        miscMetricAddCmd.setMiscMetricCO(miscMetricCO);

        Response response = metricsService.addMiscMetric(miscMetricAddCmd);

        Assert.assertTrue(response.isSuccess());
    }
}
