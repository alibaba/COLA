package com.alibaba.craftsman.app;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.runner.ColaTestRunner;
import com.alibaba.craftsman.api.UserProfileServiceI;
import com.alibaba.craftsman.dto.RefreshScoreCmd;
import com.alibaba.craftsman.dto.UserProfileAddCmd;
import com.alibaba.craftsman.dto.UserProfileGetQry;
import com.alibaba.craftsman.dto.UserProfileUpdateCmd;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import com.alibaba.craftsman.tunnel.database.UserProfileTunnel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * UserProfileCmdExeTest
 *
 * @author Frank Zhang
 * @date 2019-02-28 7:43 PM
 */
@RunWith(ColaTestRunner.class)
@ColaMockConfig(mocks={MetricTunnel.class, UserProfileTunnel.class})
public class UserProfileCmdExeTest extends MockTestBase {

    @Autowired
    private UserProfileServiceI userProfileService;

    @Autowired
    private UserProfileTunnel userProfileTunnel;

    private String userId;

    public static final String TEST_USER_NAME = "Frank";
    public static final String TEST_DEP = "ICBU";

    @Before
    public void init(){
        userId = "UserProfileCmdExeTest1000";
    }

    public static UserProfileAddCmd prepareCommand(String userId, String role){
        UserProfileAddCmd userProfileAddCmd = new UserProfileAddCmd();
        userProfileAddCmd.setOperater("testSuccessAdd");
        UserProfileCO userProfileCO = new UserProfileCO();
        userProfileCO.setUserId(userId);
        userProfileCO.setDep(TEST_DEP);
        userProfileCO.setRole(role);
        userProfileCO.setIsManager(UserProfileCO.IS_MANAGER);
        userProfileCO.setUserName(TEST_USER_NAME);
        userProfileAddCmd.setUserProfileCO(userProfileCO);
        return userProfileAddCmd;
    }

    @Test
    public void testSuccessAdd(){
        UserProfileAddCmd userProfileAddCmd = prepareCommand(this.userId, UserProfileCO.DEV_ROLE);

        Response response = userProfileService.addUserProfile(userProfileAddCmd);
        Assert.assertTrue(response.isSuccess());

        RefreshScoreCmd refreshScoreCmd = new RefreshScoreCmd(userId);
        userProfileService.refreshScore(refreshScoreCmd);

        UserProfileGetQry userProfileGetQry = new UserProfileGetQry();
        userProfileGetQry.setUserId(userId);
        UserProfileCO result = userProfileService.getUserProfileBy(userProfileGetQry).getData();
        Assert.assertEquals(TEST_USER_NAME, result.getUserName());
        Assert.assertEquals(TEST_DEP, result.getDep());
    }

    @Test
    public void testSuccessUpdate(){
        //create
        testSuccessAdd();

        //update
        UserProfileUpdateCmd userProfileUpdateCmd = new UserProfileUpdateCmd();
        UserProfileCO userProfileCO = new UserProfileCO();
        userProfileCO.setUserId(userId);
        userProfileCO.setRole(UserProfileCO.QA_ROLE);
        userProfileCO.setTechContributionScore(100);
        userProfileCO.setTechInfluenceScore(200);
        userProfileUpdateCmd.setUserProfileCO(userProfileCO);
        userProfileUpdateCmd.setOperater("testSuccessUpdate");

        Response response = userProfileService.updateUserProfile(userProfileUpdateCmd);
        Assert.assertTrue(response.isSuccess());

        //query
        UserProfileGetQry userProfileGetQry = new UserProfileGetQry();
        userProfileGetQry.setUserId(userId);
        UserProfileCO result = userProfileService.getUserProfileBy(userProfileGetQry).getData();
        Assert.assertEquals(TEST_USER_NAME, result.getUserName());
        Assert.assertEquals(100, result.getTechContributionScore(), 0.01);
        Assert.assertEquals(TEST_DEP, result.getDep());
    }

    @After
    public void cleanup(){
        userProfileTunnel.delete(userId);
    }
}
