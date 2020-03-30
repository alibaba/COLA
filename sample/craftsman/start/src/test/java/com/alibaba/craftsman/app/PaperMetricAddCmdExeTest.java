package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.PaperMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.PaperMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PaperMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:46 AM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class PaperMetricAddCmdExeTest extends MockTestBase {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testPaperMetricAddSuccess(){
        PaperMetricAddCmd paperMetricAddCmd = new PaperMetricAddCmd();
        PaperMetricCO paperMetricCO = new PaperMetricCO();
        paperMetricCO.setOwnerId("PaperMetricAddCmdExeTest_098872");
        paperMetricCO.setPaperName("paperName");
        paperMetricCO.setPaperDesc("paper Description");
        paperMetricCO.setMagazine("IEEE");
        paperMetricCO.setPaperLink("http://www.alibaba.com");
        paperMetricAddCmd.setPaperMetricCO(paperMetricCO);

        Response response = metricsService.addPaperMetric(paperMetricAddCmd);

        Assert.assertTrue(response.isSuccess());
    }
}
