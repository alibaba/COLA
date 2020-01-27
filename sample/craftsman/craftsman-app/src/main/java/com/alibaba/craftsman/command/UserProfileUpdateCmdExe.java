package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.convertor.UserProfileConvertor;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.UserProfileUpdateCmd;
import com.alibaba.craftsman.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Executor
public class UserProfileUpdateCmdExe implements ExecutorI<Response, UserProfileUpdateCmd> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Response execute(UserProfileUpdateCmd cmd) {
        UserProfile userProfile = UserProfileConvertor.toEntity(cmd.getUserProfileCO());
        userProfileRepository.update(userProfile);
        return Response.buildSuccess();
    }
}