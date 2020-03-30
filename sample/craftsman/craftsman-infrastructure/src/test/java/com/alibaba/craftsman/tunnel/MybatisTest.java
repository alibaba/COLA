package com.alibaba.craftsman.tunnel;

import com.alibaba.craftsman.tunnel.database.MetricTunnel;
import com.alibaba.craftsman.tunnel.database.dataobject.MetricDO;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * MybatisTest
 *
 * @author Frank Zhang
 * @date 2019-02-27 4:38 PM
 */
public class MybatisTest {
    SqlSession sqlSession;
    MetricTunnel metricTunnel;

    @Before
    public void before() {
        sqlSession = Mybatis3Utils.getCurrentSqlSession();
        metricTunnel = sqlSession.getMapper(MetricTunnel.class);
    }

    @After
    public void after() {
        Mybatis3Utils.closeCurrentSession();
    }

    @Test
    public void insert() {
        MetricDO metricDO = new MetricDO();
        metricDO.setMainMetric("mainTest");
        metricDO.setSubMetric("subTest");
        metricDO.setUserId("12345");
        metricDO.setMetricItem("{\"patentName\": \"Leads重构\", \"level\": \"PROJECT\"}");

        metricTunnel.create(metricDO);
        sqlSession.commit();
    }

}
