package com.alibaba.craftsman.domain.metrics.appquality;

import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.domain.metrics.MetricItem;
import lombok.Data;

@Data
public class AppMetricItem extends MetricItem {
    private final static Logger logger = LoggerFactory.getLogger(AppMetricItem.class);

    private String appName;//应用名称
    private int cyclomaticComplexityCount;//圈复杂度超标的数目
    private int duplicatedMethodCount;//重复代码的数目
    private int longMethodCount;//长方法的数目
    private int blockedCodeConductCount;//不符合编码标准的数目

    private static final int FULL_SCORE=100;
    private static final int STEP_SIZE=10;
    private static final int STEP_MINUS_SCORE = 1;

    @Override
    public double calculateScore() {
        double score = FULL_SCORE;
        score = duductScore(score, cyclomaticComplexityCount);
        score = duductScore(score, duplicatedMethodCount);
        score = duductScore(score, longMethodCount);
        score = duductScore(score, blockedCodeConductCount);
        logger.debug("Calculated App score is "+score );
        return score;
    }

    private double duductScore(double score, int count) {
        for (int counter = STEP_SIZE; counter <= count; counter = counter + STEP_SIZE) {
            score = score - STEP_MINUS_SCORE;
        }
        return score;
    }
}