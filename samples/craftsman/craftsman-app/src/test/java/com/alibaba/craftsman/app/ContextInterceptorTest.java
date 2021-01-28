package com.alibaba.craftsman.app;

import com.alibaba.craftsman.dto.UserProfileAddCmd;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
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
        userProfileAddCmd.setUserProfileCO(new UserProfileCO());

//        ContextInterceptor contextInterceptor = new ContextInterceptor();
//        contextInterceptor.preIntercept(userProfileAddCmd);
    }

    @Test
    public void testOperatorContext(){
        UserProfileAddCmd userProfileAddCmd = new UserProfileAddCmd();
        userProfileAddCmd.setUserProfileCO(new UserProfileCO());
        userProfileAddCmd.setOperater("Frank");

//        ContextInterceptor contextInterceptor = new ContextInterceptor();
//        contextInterceptor.preIntercept(userProfileAddCmd);
    }
}
