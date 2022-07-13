package com.alibaba.craftsman.gatewayimpl;

import com.alibaba.craftsman.gatewayimpl.database.MetricMapper;
import com.alibaba.craftsman.gatewayimpl.database.dataobject.MetricDO;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;

/**
 * MybatisTest
 *
 * @author Frank Zhang
 * @date 2019-02-27 4:38 PM
 */
public class MybatisTest {
    SqlSession sqlSession;
    MetricMapper metricMapper;

    @Before
    public void before() {
        sqlSession = Mybatis3Utils.getCurrentSqlSession();
        metricMapper = sqlSession.getMapper(MetricMapper.class);
    }

    @After
    public void after() {
        Mybatis3Utils.closeCurrentSession();
    }

    //@Test
    public void insert() {
        MetricDO metricDO = new MetricDO();
        metricDO.setMainMetric("mainTest");
        metricDO.setSubMetric("subTest");
        metricDO.setUserId("12345");
        metricDO.setCreator("Frank");
        metricDO.setModifier("Frank");
        metricDO.setMetricItem("{\"patentName\": \"Leads重构\", \"level\": \"PROJECT\"}");

        metricMapper.create(metricDO);
        sqlSession.commit();
    }

}
