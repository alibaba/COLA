package com.alibaba.craftsman.domain.metrics.weight;

/**
 * 非技术人员不需要考核
 */
public class OtherWeight extends Weight{

    public static OtherWeight singleton= new OtherWeight();

    @Override
    public double getAppQualityWeight() {
        return 0;
    }

    @Override
    public double getTechInfluenceWeight() {
        return 0;
    }

    @Override
    public double getTechContributionWeight() {
        return 0;
    }

    @Override
    public double getDevQualityWeight() {
        return 0;
    }
}

