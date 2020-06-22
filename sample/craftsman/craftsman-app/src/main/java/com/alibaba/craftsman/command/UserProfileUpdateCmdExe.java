package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.convertor.UserProfileConvertor;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.UserProfileUpdateCmd;
import com.alibaba.craftsman.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserProfileUpdateCmdExe{

    @Resource
    private UserProfileRepository userProfileRepository;

    public Response execute(UserProfileUpdateCmd cmd) {
        UserProfile userProfile = UserProfileConvertor.toEntity(cmd.getUserProfileCO());
        userProfileRepository.update(userProfile);
        return Response.buildSuccess();
    }
}