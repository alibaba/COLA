package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.PatentMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.PatentMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PatentMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:57 AM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class PatentMetricAddCmdExeTest extends MockTestBase {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testPatentMetricAddSuccess(){
        PatentMetricAddCmd patentMetricAddCmd = new PatentMetricAddCmd();
        patentMetricAddCmd.setOperater("jack ma");
        PatentMetricCO patentMetricCO = new PatentMetricCO();
        patentMetricCO.setOwnerId("PatentMetricAddCmdExeTest_627327");
        patentMetricCO.setAuthorType(PatentMetricCO.FIRST_AUTHOR_TYPE);
        patentMetricCO.setPatentName("patentName");
        patentMetricCO.setPatentDesc("This is a very valuable patent");
        patentMetricCO.setPatentNo("73499992323");
        patentMetricCO.setPatentUrl("http://www.alibaba.com");
        patentMetricAddCmd.setPatentMetricCO(patentMetricCO);

        Response response = metricsService.addPatentMetric(patentMetricAddCmd);

        Assert.assertTrue(response.isSuccess());
    }
}
