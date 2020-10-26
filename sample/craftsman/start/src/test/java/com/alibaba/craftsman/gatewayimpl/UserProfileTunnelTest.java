package com.alibaba.craftsman.gatewayimpl;

import com.alibaba.craftsman.gatewayimpl.database.UserProfileMapper;
import com.alibaba.craftsman.gatewayimpl.database.dataobject.UserProfileDO;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is Tunnel Test, no need to mock, and no regression needed as well
 *
 * @author Frank Zhang
 * @date 2019-02-27 5:31 PM
 */
public class UserProfileTunnelTest {
    @Autowired
    private UserProfileMapper userProfileMapper;

    public void testCRUD(){
        String userId = Math.random()+"UserProfileTunnelTest";

        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setUserId(userId);
        userProfileDO.setDep("alibaba");
        userProfileDO.setIsManager("n");
        userProfileDO.setUserName("Frank");
        userProfileDO.setRole("DEV");

        userProfileMapper.create(userProfileDO);

        userProfileDO = userProfileMapper.getByUserId(userId);
        Assert.assertEquals(userId, userProfileDO.getUserId());

        userProfileMapper.delete(userId);
        Assert.assertNull(userProfileMapper.getByUserId(userId));
    }
}
