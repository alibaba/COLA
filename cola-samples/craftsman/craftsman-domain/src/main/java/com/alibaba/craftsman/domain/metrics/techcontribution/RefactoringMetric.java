package com.alibaba.craftsman.domain.metrics.techcontribution;

import com.alibaba.craftsman.domain.metrics.*;

/**
 * 重构指标
 * @author xueliang.sxl, alisa.hsh, xiangning.lxn
 */
public class RefactoringMetric extends SubMetric {

    public RefactoringMetric(){
        this.subMetricType = SubMetricType.Refactoring;
    }

    public RefactoringMetric(MainMetric parent) {
        this.parent = parent;
        parent.addSubMetric(this);
        this.subMetricType = SubMetricType.Refactoring;
    }

    @Override
    public double getWeight() {
        return  metricOwner.getWeight().getUnanimousWeight();
    }
}
