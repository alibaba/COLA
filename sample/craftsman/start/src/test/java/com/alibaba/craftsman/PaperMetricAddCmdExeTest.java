package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.PaperMetricAddCmd;
import com.alibaba.craftsman.dto.SharingMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.PaperMetricCO;
import com.alibaba.craftsman.dto.clientobject.SharingMetricCO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * PaperMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:46 AM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PaperMetricAddCmdExeTest {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
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
