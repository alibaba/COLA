package com.alibaba.craftsman.app;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.MetricDeleteCmd;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class MetricDeleteCmdExeTest extends MockTestBase {

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
