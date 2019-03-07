package com.alibaba.craftsman;

import com.alibaba.craftsman.domain.metrics.MainMetricType;
import com.alibaba.craftsman.domain.metrics.SubMetricType;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import com.alibaba.craftsman.tunnel.database.UserProfileTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;
import com.alibaba.craftsman.tunnel.database.dataobject.UserProfileDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * UserProfileTunnelTest
 *
 * @author Frank Zhang
 * @date 2019-02-27 5:31 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class UserProfileTunnelTest {
    @Autowired
    private UserProfileTunnel userProfileTunnel;

    @Test
    public void testCRUD(){
        String userId = Math.random()+"UserProfileTunnelTest";

        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setUserId(userId);
        userProfileDO.setDep("alibaba");
        userProfileDO.setIsManager("n");
        userProfileDO.setUserName("Frank");
        userProfileDO.setRole("DEV");
        userProfileDO.setCreator("Frank");
        userProfileDO.setModifier("Frank");

        userProfileTunnel.create(userProfileDO);

        userProfileDO = userProfileTunnel.getByUserId(userId);
        Assert.assertEquals(userId, userProfileDO.getUserId());

        userProfileTunnel.delete(userId);
        Assert.assertNull(userProfileTunnel.getByUserId(userId));
    }
}
