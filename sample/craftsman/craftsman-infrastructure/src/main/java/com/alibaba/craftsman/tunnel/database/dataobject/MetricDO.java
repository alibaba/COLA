package com.alibaba.craftsman.tunnel.database.dataobject;

import com.alibaba.cola.tunnel.DataObject;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MetricDO extends DataObject {
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
