package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.convertor.UserProfileConvertor;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.repository.UserProfileRepository;
import com.alibaba.craftsman.dto.UserProfileUpdateCmd;
import org.springframework.beans.factory.annotation.Autowired;

@Command
public class UserProfileUpdateCmdExe implements CommandExecutorI<Response, UserProfileUpdateCmd> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Response execute(UserProfileUpdateCmd cmd) {
        UserProfile userProfile = UserProfileConvertor.toEntity(cmd.getUserProfileCO());
        userProfileRepository.update(userProfile);
        return Response.buildSuccess();
    }
}