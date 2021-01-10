package com.alibaba.craftsman.gatewayimpl.database.dataobject;


public class MetricDO extends BaseDO{
    private String id;
    /**
     * 域账号
     */
    private String userId;
    /**
     * 主度量
     */
    private String mainMetric;
    /**
     * 子度量
     */
    private String subMetric;
    /**
     * 度量条目
     */
    private String metricItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMainMetric() {
        return mainMetric;
    }

    public void setMainMetric(String mainMetric) {
        this.mainMetric = mainMetric;
    }

    public String getSubMetric() {
        return subMetric;
    }

    public void setSubMetric(String subMetric) {
        this.subMetric = subMetric;
    }

    public String getMetricItem() {
        return metricItem;
    }

    public void setMetricItem(String metricItem) {
        this.metricItem = metricItem;
    }
}
