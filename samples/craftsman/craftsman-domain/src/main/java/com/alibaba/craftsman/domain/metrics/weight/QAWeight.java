package com.alibaba.craftsman.domain.metrics.weight;

/**
 * 测试的分数权重占比
 */
public class QAWeight extends Weight{

    public static QAWeight singleton= new QAWeight();

    @Override
    public double getAppQualityWeight() {
        return 10 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getTechInfluenceWeight() {
        return 60 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getTechContributionWeight() {
        return 20 / WEIGHT_PERCENTAGE;
    }

    @Override
    public double getDevQualityWeight() {
        return 10 / WEIGHT_PERCENTAGE;
    }
}
