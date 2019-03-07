package com.alibaba.craftsman.domain.metrics.techinfluence;

import lombok.Getter;

/**
 * 论文专利作者类型枚举
 *
 * @author Frank Zhang
 * @date 2018-09-20 3:28 PM
 */
public enum AuthorType {

    FIRST_AUTHOR(AuthorType.FIRST_AUTHOR_PATENT_SCORE, "专利或者论文的第一作者"),
    OTHER_AUTHOR(AuthorType.OTHER_AUTHOR_PATENT_SCORE, "专利或论文的其他作者");

    private static final double FIRST_AUTHOR_PATENT_SCORE = 20;
    private static final double OTHER_AUTHOR_PATENT_SCORE = 5;

    @Getter
    private double score;
    private String desc;

    private AuthorType(double score, String desc) {
        this.score = score;
        this.desc = desc;
    }

}
