package com.alibaba.craftsman;


import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class MetricDeleteCmdExeTest {

    @Autowired
    private MetricsServiceI metricsService;

    @Test
    public void testSuccess(){
        MetricDeleteCmd cmd =  new MetricDeleteCmd();
        cmd.setMetricId("1013");
        cmd.setOperater("MetricDeleteCmdExeTest");

        Response response = metricsService.deleteMetric(cmd);

        Assert.assertTrue(response.isSuccess());

    }
}
