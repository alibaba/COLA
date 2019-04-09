package com.alibaba.craftsman.convertor;


import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.craftsman.context.UserContext;
import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;

/**
 * @author frankzhang
 */
public class MetricConvertor implements ConvertorI {

    public static MetricDO toDataObject(MetricItem metricItem){
        MetricDO metricDO = new MetricDO();
        UserContext userContext = (UserContext)metricItem.getContext().getContent();
        metricDO.setCreator(userContext.getOperator());
        metricDO.setModifier(userContext.getOperator());
        metricDO.setUserId(metricItem.getMetricOwner().getUserId());
        metricDO.setMainMetric(metricItem.getSubMetric().getParent().getCode());
        metricDO.setSubMetric(metricItem.getSubMetric().getCode());
        metricDO.setMetricItem(metricItem.toJsonString());
        return metricDO;
    }

}
