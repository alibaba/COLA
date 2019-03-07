package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.dto.ATAMetricAddCmd;
import com.alibaba.craftsman.dto.SharingMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import com.alibaba.craftsman.dto.clientobject.SharingMetricCO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * SharingMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-02 5:08 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class SharingMetricAddCmdExeTest {
    @Autowired
    private MetricsServiceI metricsService;

    @Test
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
