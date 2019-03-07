package com.alibaba.craftsman.convertor;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.craftsman.context.LoginUser;
import com.alibaba.craftsman.domain.user.Role;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.craftsman.dto.clientobject.UserProfileCO;
import com.alibaba.craftsman.tunnel.database.dataobject.UserProfileDO;
import org.springframework.beans.BeanUtils;

public class UserProfileConvertor implements ConvertorI {

    public static UserProfile toEntity(UserProfileCO userProfileCO, Context context){
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userProfileCO, userProfile);
        userProfile.setRole(Role.valueOf(userProfileCO.getRole()));
        userProfile.setContext(context);
        return userProfile;
    }

    public static UserProfileDO toDataObject(UserProfile userProfile){
        UserProfileDO userProfileDO = new UserProfileDO();
        BeanUtils.copyProperties(userProfile, userProfileDO);
        userProfileDO.setRole(userProfile.getRole().name());
        return userProfileDO;
    }

    public static UserProfileDO toDataObjectForCreate(UserProfile userProfile){
        UserProfileDO userProfileDO = toDataObject(userProfile);
        userProfileDO.setCreator(((LoginUser)userProfile.getContext().getContent()).getLoginUserId());
        userProfileDO.setModifier(((LoginUser)userProfile.getContext().getContent()).getLoginUserId());
        return userProfileDO;
    }

    public static UserProfileDO  toDataObjectForUpdate(UserProfile userProfile){
        UserProfileDO userProfileDO = toDataObject(userProfile);
        userProfileDO.setModifier(((LoginUser)userProfile.getContext().getContent()).getLoginUserId());
        return userProfileDO;
    }
}
