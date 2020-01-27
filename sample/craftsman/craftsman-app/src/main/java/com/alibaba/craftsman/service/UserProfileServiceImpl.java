package com.alibaba.craftsman.service;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.executor.ExecutorBusI;
import com.alibaba.craftsman.api.UserProfileServiceI;
import com.alibaba.craftsman.dto.*;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserProfileServiceImpl
 *
 * @author Frank Zhang
 * @date 2019-02-28 6:22 PM
 */
@Service
public class UserProfileServiceImpl implements UserProfileServiceI{
    @Autowired
    private ExecutorBusI executorBus;

    @Override
    public Response addUserProfile(UserProfileAddCmd userProfileAddCmd) {
        return (Response) executorBus.send(userProfileAddCmd);
    }

    @Override
    public Response updateUserProfile(UserProfileUpdateCmd cmd) {
        return (Response) executorBus.send(cmd);
    }

    @Override
    public Response refreshScore(RefreshScoreCmd cmd) {
        return (Response) executorBus.send(cmd);
    }

    @Override
    public SingleResponse<UserProfileCO> getUserProfileBy(UserProfileGetQry qry) {
        return (SingleResponse) executorBus.send(qry);
    }

    @Override
    public MultiResponse<UserProfileCO> listUserProfileBy(UserProfileListQry qry) {
        return (MultiResponse) executorBus.send(qry);
    }
}
