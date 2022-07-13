package com.alibaba.craftsman.domain.gateway;

import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.craftsman.domain.metrics.SubMetric;
import com.alibaba.craftsman.domain.metrics.appquality.AppMetric;
import com.alibaba.craftsman.domain.metrics.devquality.BugMetric;

import java.util.List;

/**
 * MetricGateway
 *
 * @author Frank Zhang
 * @date 2020-07-02 12:16 PM
 */
public interface MetricGateway {
    void save(MetricItem metricItem);
    List<SubMetric> listByTechContribution(String userId);
    List<SubMetric> listByTechInfluence(String userId);
    BugMetric getBugMetric(String userId);
    AppMetric getAppMetric(String userId);
}
