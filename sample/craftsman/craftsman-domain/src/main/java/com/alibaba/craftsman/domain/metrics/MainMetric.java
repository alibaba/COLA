package com.alibaba.craftsman.domain.metrics;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Metric, 一个主度量（MainMetric）是由多个子度量（SubMetric）组成的。
 *
 * @author Frank Zhang
 * @date 2018-08-28 2:00 PM
 */
@Data
public abstract class MainMetric extends Metric{


    protected MainMetricType metricMainType;

    protected List<SubMetric> subMetrics = new ArrayList<>();

    public MainMetric(){
    }

    public void addSubMetric(SubMetric metric){
        subMetrics.add(metric);
    }

    @Override
    public String getName() {
        return metricMainType.getMetricName();
    }

    @Override
    public String getCode(){
        return metricMainType.getMetricCode();
    }


    @Override
    public double calculateScore() {
        double mainMetricScore = 0;
        for (Metric subMetric : subMetrics) {
            mainMetricScore = mainMetricScore + subMetric.calculateScore() * subMetric.getWeight();
        }
        return mainMetricScore;
    }
}
