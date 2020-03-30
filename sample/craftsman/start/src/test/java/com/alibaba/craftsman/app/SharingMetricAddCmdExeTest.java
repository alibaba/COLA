package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.SharingMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.SharingMetricCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SharingMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-02 5:08 PM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class})
public class SharingMetricAddCmdExeTest extends MockTestBase {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
    @ExcludeCompare(fields = {"id","userId"})
    public void testSharingMetricAddSuccess(){
        SharingMetricAddCmd sharingMetricAddCmd = new SharingMetricAddCmd();
        SharingMetricCO sharingMetricCO = new SharingMetricCO();
        sharingMetricCO.setOwnerId("testSharingMetricAddSuccess_089765");
        sharingMetricCO.setSharingName("Structured thinking");
        sharingMetricCO.setSharingDate("2018-09-09");
        sharingMetricCO.setSharingLink("sharing.com");
        sharingMetricCO.setSharingScope(sharingMetricCO.TEAM_SCOPE);
        sharingMetricAddCmd.setSharingMetricCO(sharingMetricCO);

        Response response = metricsService.addSharingMetric(sharingMetricAddCmd);

        Assert.assertTrue(response.isSuccess());
    }
}
