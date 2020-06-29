package com.alibaba.cola.mock.agent.model;

/**
 * @author shawnzhan.zxy
 * @since 2019/04/30
 */
public enum TranslateType {
    DEFAULT_CONSTRUCTOR("DEFAULT_CONSTRUCTOR"),
    ONLINE_RECORD("ONLINE_RECORD"),

    ;

    private String type;

    TranslateType(String type){
        this.type = type;
    }
}
