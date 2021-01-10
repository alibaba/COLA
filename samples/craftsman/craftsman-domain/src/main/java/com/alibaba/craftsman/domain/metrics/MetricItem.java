package com.alibaba.craftsman.domain.metrics;

import com.alibaba.cola.domain.EntityObject;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.domain.user.UserProfile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * MetricItem
 * 指标项，多个指标项可以构成一个指标
 * @author Frank Zhang
 * @date 2018-07-04 1:23 PM
 */
@Data
public abstract class MetricItem extends EntityObject implements Measurable{

    private static Logger logger = LoggerFactory.getLogger(MetricItem.class);

    /**
     * The metric this MetricItem belongs to
     */
    @JSONField(serialize = false)
    private SubMetric subMetric;

    /**
     * The owner of this MetricItem
     */
    @JSONField(serialize = false)
    private UserProfile metricOwner;

    public void setSubMetric(SubMetric subMetric){
        this.subMetric = subMetric;
        this.metricOwner = subMetric.getMetricOwner();
    }
    /**
     * 将度量项的转成JSON
     * @return
     */
    public String toJsonString() {
        String jsonStr = JSON.toJSONString(this, JSONPropertyFilter.singleton);
        logger.debug("\n From : " + this + " \n To: " + jsonStr);
        return jsonStr;
    }

}
