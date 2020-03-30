package com.alibaba.craftsman.command;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.CommandExecutorI;
import com.alibaba.cola.dto.Response;
import com.alibaba.craftsman.convertor.UserProfileConvertor;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.repository.UserProfileRepository;
import com.alibaba.craftsman.dto.UserProfileAddCmd;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserProfileAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-02-28 6:25 PM
 */
@Command
public class UserProfileAddCmdExe implements CommandExecutorI<Response, UserProfileAddCmd> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Response execute(UserProfileAddCmd cmd) {
        UserProfile userProfile = UserProfileConvertor.toEntity(cmd.getUserProfileCO());
        userProfileRepository.create(userProfile);
        return Response.buildSuccess();
    }
}
