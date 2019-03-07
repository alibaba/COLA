package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.PatentMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.PatentMetricCO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * PatentMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-03 11:57 AM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PatentMetricAddCmdExeTest {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
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
