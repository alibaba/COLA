package com.alibaba.craftsman.domain.user;

/**
 * Role Enumeration
 *
 * @author Frank Zhang
 * @date 2018-09-13 12:25 PM
 */
public enum Role {
    DEV("开发"),
    QA( "测试"),
    OTHER("非技术岗");

    public String desc;

    Role(String desc){
        this.desc = desc;
    }
}
