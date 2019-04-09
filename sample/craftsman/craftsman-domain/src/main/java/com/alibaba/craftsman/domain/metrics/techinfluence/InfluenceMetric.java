package com.alibaba.craftsman.domain.metrics.techinfluence;

import com.alibaba.craftsman.domain.metrics.MainMetric;
import com.alibaba.craftsman.domain.metrics.MainMetricType;
import com.alibaba.craftsman.domain.user.UserProfile;
import lombok.Data;

/**
 * InfluenceMetric
 * 影响力指标
 * @author Frank Zhang
 * @date 2018-07-04 1:24 PM
 */
@Data
public class InfluenceMetric extends MainMetric {
    private ATAMetric ataMetric;
    private PatentMetric patentMetric;
    private SharingMetric sharingMetric;
    private PaperMetric paperMetric;

    public InfluenceMetric(UserProfile metricOwner){
        this.metricOwner = metricOwner;
        metricOwner.setInfluenceMetric(this);
        this.metricMainType = MainMetricType.TECH_INFLUENCE;
    }

    @Override
    public double getWeight() {
        return  metricOwner.getWeight().getTechInfluenceWeight();
    }
}
