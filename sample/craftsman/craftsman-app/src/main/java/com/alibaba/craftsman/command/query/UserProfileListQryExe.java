package com.alibaba.craftsman.command.query;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.executor.Executor;
import com.alibaba.cola.executor.ExecutorI;
import com.alibaba.craftsman.dto.UserProfileListQry;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.tunnel.database.UserProfileTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.UserProfileDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Executor
public class UserProfileListQryExe implements ExecutorI<Response, UserProfileListQry> {

    @Autowired
    private UserProfileTunnel userProfileTunnel;

    @Override
    public MultiResponse<UserProfileCO> execute(UserProfileListQry qry) {
        List<UserProfileDO> userProfileDOList = userProfileTunnel.listByDep(qry.getDep());
        List<UserProfileCO> userProfileCOList = new ArrayList<>();
        userProfileDOList.forEach(userDO -> {
            UserProfileCO userProfileCO = new UserProfileCO();
            BeanUtils.copyProperties(userDO, userProfileCO);
            userProfileCOList.add(userProfileCO);
        });
        return MultiResponse.ofWithoutTotal(userProfileCOList);
    }

}

