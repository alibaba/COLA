package com.alibaba.cola.unittest.wiremock;

import lombok.Data;

@Data
public class Account {
    /**
     * 用户号码
     */
    private long phoneNo;

    /**
     * 账户余额
     */
    private String remaining;


    private String name;
}

