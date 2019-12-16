package com.alibaba.craftsman.service;

import com.alibaba.cola.command.CommandBusI;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.craftsman.api.UserProfileServiceI;
import com.alibaba.craftsman.command.RefreshScoreCmdExe;
import com.alibaba.craftsman.command.UserProfileAddCmdExe;
import com.alibaba.craftsman.command.UserProfileUpdateCmdExe;
import com.alibaba.craftsman.command.query.UserProfileGetQryExe;
import com.alibaba.craftsman.command.query.UserProfileListQryExe;
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
    private CommandBusI commandBus;

    @Override
    public Response addUserProfile(UserProfileAddCmd userProfileAddCmd) {
        return (Response) commandBus.send(userProfileAddCmd);
    }

    @Override
    public Response updateUserProfile(UserProfileUpdateCmd cmd) {
        return (Response) commandBus.send(cmd);
    }

    @Override
    public Response refreshScore(RefreshScoreCmd cmd) {
        return (Response) commandBus.send(cmd);
    }

    @Override
    public SingleResponse<UserProfileCO> getUserProfileBy(UserProfileGetQry qry) {
        return (SingleResponse) commandBus.send(qry);
    }

    @Override
    public MultiResponse<UserProfileCO> listUserProfileBy(UserProfileListQry qry) {
        return (MultiResponse) commandBus.send(qry);
    }
}
