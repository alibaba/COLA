package com.alibaba.craftsman.tunnel.database;

import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MetricTunnel {

    int create(MetricDO dataObject);

    List<MetricDO> listByUserId(@Param("userId") String userId);

    List<MetricDO> listByMainMetric(@Param("userId") String userId, @Param("mainMetric") String mainMetric);

    List<MetricDO> listBySubMetric(@Param("userId") String userId, @Param("subMetric") String subMetric);

    int delete(@Param("id") String id, @Param("modifier") String modifier);

    MetricDO getById(@Param("id") String id);
}
