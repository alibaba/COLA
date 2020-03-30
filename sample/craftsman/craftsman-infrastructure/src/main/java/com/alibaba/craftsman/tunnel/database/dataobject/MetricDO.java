package com.alibaba.craftsman.tunnel.database.dataobject;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MetricDO{
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
}
