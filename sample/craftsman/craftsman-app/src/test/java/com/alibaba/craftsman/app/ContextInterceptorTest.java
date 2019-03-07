package com.alibaba.craftsman.app;

import com.alibaba.craftsman.context.LoginUser;
import com.alibaba.craftsman.dto.UserProfileAddCmd;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.interceptor.ContextInterceptor;
import org.junit.Assert;
import org.junit.Test;

/**
 * ContextInterceptorTest 单元测试
 *
 * @author Frank Zhang
 * @date 2019-03-01 9:38 AM
 */
public class ContextInterceptorTest {

    @Test
    public void testNoOperatorContext(){
        UserProfileAddCmd userProfileAddCmd = new UserProfileAddCmd();
        UserProfileCO userProfileCO = new UserProfileCO();
        userProfileCO.setUserId("UserProfileAddCmdExeTest" + Math.random());
        userProfileCO.setDep("Alibaba");
        userProfileCO.setIsManager(UserProfileCO.IS_MANAGER);
        userProfileCO.setUserName("Frank");
        userProfileAddCmd.setUserProfileCO(userProfileCO);

        ContextInterceptor contextInterceptor = new ContextInterceptor();
        contextInterceptor.preIntercept(userProfileAddCmd);
        String loginUserId = ((LoginUser)userProfileAddCmd.getContext().getContent()).getLoginUserId();

        Assert.assertEquals(loginUserId, ContextInterceptor.SYS_USER);
    }
}
