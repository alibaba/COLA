package com.alibaba.craftsman.domain.metrics.techinfluence;

import lombok.Getter;

/**
 * SharingScope
 * 线下分享的范围，范围不同，分值不同
 * @author Frank Zhang
 * @date 2018-07-04 3:25 PM
 */
public enum SharingScope {
    TEAM(2, "团队内分享"),
    BU(20, "BU内部分享"),
    ALIBABA(30, "集团内部分享"),
    COMMUNITY(40, "公众外部分享");


    @Getter
    private double score;
    private String desc;

    private SharingScope(double score, String desc) {
        this.score = score;
        this.desc = desc;
    }

}
