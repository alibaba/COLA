package com.alibaba.craftsman.domain.metrics.appquality;

import com.alibaba.craftsman.domain.metrics.MainMetric;
import com.alibaba.craftsman.domain.metrics.MainMetricType;
import com.alibaba.craftsman.domain.metrics.devquality.BugMetric;
import com.alibaba.craftsman.domain.user.UserProfile;

public class AppQualityMetric extends MainMetric {

    private AppMetric appMetric;

    public AppQualityMetric(UserProfile metricOwner){
        this.metricOwner = metricOwner;
        metricOwner.setAppQualityMetric(this);
        this.metricMainType = MainMetricType.APP_QUALITY;
    }

    @Override
    public double getWeight() {
        return metricOwner.getWeight().getAppQualityWeight();
    }
}
