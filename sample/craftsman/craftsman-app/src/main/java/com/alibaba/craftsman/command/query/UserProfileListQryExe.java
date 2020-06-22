package com.alibaba.craftsman.command.query;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.craftsman.dto.UserProfileListQry;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.tunnel.database.UserProfileTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.UserProfileDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserProfileListQryExe{

    @Resource
    private UserProfileTunnel userProfileTunnel;

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

