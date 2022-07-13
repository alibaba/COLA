package com.alibaba.craftsman.domain.metrics.techcontribution;

import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * CodeReview指标项
 * @author xueliang.sxl, alisa.hsh, xiangning.lxn
 */
@Data
public class CodeReviewMetricItem extends MetricItem {

    /**
     * 评审id
     */
    private String reviewId;

    /**
     * 评论数量
     */
    private int noteCount;


    /**
     * 文档链接
     */
    private String reviewDocLink;

    /**
     * 每条评论0.1分
     */
    private static double CODE_REVIEW_SCORE = 0.1;

    public CodeReviewMetricItem(){

    }


    public static CodeReviewMetricItem valueOf(String json){
        return JSON.parseObject(json, CodeReviewMetricItem.class);
    }

    /**
     * 计算当前度量项分数
     * @return
     */
    @Override
    public double calculateScore() {
        return noteCount * CODE_REVIEW_SCORE;
    }
}
