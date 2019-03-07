package com.alibaba.craftsman.convertor;


import com.alibaba.cola.convertor.ConvertorI;
import com.alibaba.craftsman.context.LoginUser;
import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;

/**
 * @author frankzhang
 */
public class MetricConvertor implements ConvertorI {

    public static MetricDO toDataObject(MetricItem metricItem){
        MetricDO metricDO = new MetricDO();
        LoginUser loginUser = (LoginUser)metricItem.getContext().getContent();
        metricDO.setCreator(loginUser.getLoginUserId());
        metricDO.setModifier(loginUser.getLoginUserId());
        metricDO.setUserId(metricItem.getMetricOwner().getUserId());
        metricDO.setMainMetric(metricItem.getSubMetric().getParent().getCode());
        metricDO.setSubMetric(metricItem.getSubMetric().getCode());
        metricDO.setMetricItem(metricItem.toJsonString());
        return metricDO;
    }

}
