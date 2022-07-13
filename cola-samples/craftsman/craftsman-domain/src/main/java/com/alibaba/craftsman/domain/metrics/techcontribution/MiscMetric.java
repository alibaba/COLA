package com.alibaba.craftsman.domain.metrics.techcontribution;

import com.alibaba.craftsman.domain.metrics.*;

/**
 * Miscellaneous，其他度量，任何的技术亮点都可以添加
 * @author frankzhang
 */
public class MiscMetric extends SubMetric {

    public MiscMetric(){
        this.subMetricType = SubMetricType.Misc;
    }

    public MiscMetric(MainMetric parent) {
        this.parent = parent;
        parent.addSubMetric(this);
        this.subMetricType = SubMetricType.Misc;
    }

    @Override
    public double getWeight() {
        return metricOwner.getWeight().getUnanimousWeight();
    }
}
