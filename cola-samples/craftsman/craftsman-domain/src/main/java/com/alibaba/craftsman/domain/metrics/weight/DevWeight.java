package com.alibaba.craftsman.domain.metrics.weight;

/**
 * 开发的总分计算权重
 */
public class DevWeight extends Weight{

    public static DevWeight singleton= new DevWeight();

    @Override
    public double getAppQualityWeight() {
        return  20 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getTechInfluenceWeight() {
        return 30 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getTechContributionWeight() {
        return 30 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getDevQualityWeight() {
        return 20 / WEIGHT_PERCENTAGE;
    }
}
