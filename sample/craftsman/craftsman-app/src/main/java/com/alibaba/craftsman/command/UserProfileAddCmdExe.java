package com.alibaba.craftsman.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.convertor.UserProfileConvertor;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.UserProfileAddCmd;
import com.alibaba.craftsman.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserProfileAddCmdExe
 *
 * @author Frank Zhang
 * @date 2019-02-28 6:25 PM
 */
@Executor
public class UserProfileAddCmdExe implements ExecutorI<Response, UserProfileAddCmd> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Response execute(UserProfileAddCmd cmd) {
        UserProfile userProfile = UserProfileConvertor.toEntity(cmd.getUserProfileCO());
        userProfileRepository.create(userProfile);
        return Response.buildSuccess();
    }
}
