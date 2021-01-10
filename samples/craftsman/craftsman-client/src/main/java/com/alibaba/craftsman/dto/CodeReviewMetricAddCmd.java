package com.alibaba.craftsman.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * CodeReviewMetricAddCmd
 *
 * @author Frank Zhang
 * @date 2019-03-01 10:09 AM
 */
@Data
public class CodeReviewMetricAddCmd extends CommonCommand{

    @NotEmpty
    private String ownerId;

    @NotEmpty
    private String reviewId;

    /**
     * 评论数
     */
    @Positive
    private int noteCount;

    /**
     * 文档链接
     */
    private String reviewDocLink;
}
