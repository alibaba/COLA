package com.alibaba.craftsman;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.alibaba.craftsman.api.MetricsServiceI;
import com.alibaba.craftsman.api.UserProfileServiceI;
import com.alibaba.craftsman.dto.ATAMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ATAMetricAddCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-03-01 6:18 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class ATAMetricAddCmdExeTest {

    @Autowired
    private MetricsServiceI metricsService;

    private String userId;

    @Before
    public void init(){
        userId = "ATAMetricAddCmdExeTest" + System.currentTimeMillis();
    }

    public static ATAMetricAddCmd prepareCommand(String userId){
        ATAMetricAddCmd ataMetricAddCmd = new ATAMetricAddCmd();
        ATAMetricCO ataMetricCO = new ATAMetricCO();
        ataMetricCO.setOwnerId(userId);
        ataMetricCO.setTitle("testATAMetricAddSuccess");
        ataMetricCO.setUrl("sharingLink");
        ataMetricCO.setCommentCount(14);
        ataMetricCO.setFavoriteCount(49);
        ataMetricCO.setHitCount(299);
        ataMetricCO.setThumbsUpCount(89);
        ataMetricAddCmd.setAtaMetricCO(ataMetricCO);
        return ataMetricAddCmd;
    }

    @Test
    public void testATAMetricAddSuccess(){
        ATAMetricAddCmd ataMetricAddCmd = prepareCommand(userId);
        Response response = metricsService.addATAMetric(ataMetricAddCmd);
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testATAMetricAddWithNoAuthor(){
        ATAMetricAddCmd ataMetricAddCmd = new ATAMetricAddCmd();
        ATAMetricCO ataMetricCO = new ATAMetricCO();
        ataMetricAddCmd.setAtaMetricCO(ataMetricCO);

        Response response = metricsService.addATAMetric(ataMetricAddCmd);

        Assert.assertFalse(response.isSuccess());
    }
}
