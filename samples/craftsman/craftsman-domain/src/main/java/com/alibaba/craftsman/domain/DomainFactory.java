package com.alibaba.craftsman.domain;

import com.alibaba.craftsman.domain.user.UserProfile;

public class DomainFactory {

    public static UserProfile getUserProfile(){
        return new UserProfile();
    }

}
