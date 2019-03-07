package com.alibaba.craftsman.context;

import lombok.Data;

/**
 * LoginUser
 *
 * @author Frank Zhang
 * @date 2019-02-28 7:08 PM
 */
@Data
public class LoginUser {
    private String loginUserId;
    private String loginUserName;
    private String loginUserRole;
    private String loginUserPrivilege;
}
