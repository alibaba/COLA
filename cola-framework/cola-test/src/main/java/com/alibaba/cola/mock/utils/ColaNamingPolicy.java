package com.alibaba.cola.mock.utils;

import org.springframework.cglib.core.DefaultNamingPolicy;

public class ColaNamingPolicy extends DefaultNamingPolicy {
    
    public static final ColaNamingPolicy INSTANCE = new ColaNamingPolicy();
    
    @Override
    protected String getTag() {
        return Constants.COLAMOCK_PROXY_FLAG;
    }
}