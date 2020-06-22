package com.alibaba.craftsman.command.query;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.craftsman.dto.UserProfileGetQry;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.tunnel.database.UserProfileTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.UserProfileDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserProfileGetQryExe {

    @Resource
    private UserProfileTunnel userProfileTunnel;

    public SingleResponse<UserProfileCO> execute(UserProfileGetQry qry) {
        UserProfileDO userProfileDO = userProfileTunnel.getByUserId(qry.getUserId());
        UserProfileCO userProfileCO = new UserProfileCO();
        BeanUtils.copyProperties(userProfileDO, userProfileCO);
        return SingleResponse.of(userProfileCO);
    }

}
