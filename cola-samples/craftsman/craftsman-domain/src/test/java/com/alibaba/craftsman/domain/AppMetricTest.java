package com.alibaba.craftsman.domain;

import com.alibaba.craftsman.domain.metrics.appquality.AppMetric;
import com.alibaba.craftsman.domain.metrics.appquality.AppMetricItem;
import org.junit.Assert;
import org.junit.Test;

public class AppMetricTest {

    @Test
    public void testAppMetricItem(){
        AppMetricItem appMetricItem = new AppMetricItem();
        appMetricItem.setAppName("app1");
        appMetricItem.setCyclomaticComplexityCount(200);
        appMetricItem.setDuplicatedMethodCount(80);
        appMetricItem.setLongMethodCount(70);
        appMetricItem.setBlockedCodeConductCount(20);

        Assert.assertEquals(63, appMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void testAppMetric(){
        AppMetricItem appMetricItem1 = new AppMetricItem();
        appMetricItem1.setAppName("app1");
        appMetricItem1.setCyclomaticComplexityCount(200);
        appMetricItem1.setDuplicatedMethodCount(80);
        appMetricItem1.setLongMethodCount(70);
        appMetricItem1.setBlockedCodeConductCount(20);
        appMetricItem1.calculateScore();

        AppMetricItem appMetricItem2 = new AppMetricItem();
        appMetricItem2.setAppName("app2");
        appMetricItem2.setCyclomaticComplexityCount(20);
        appMetricItem2.setDuplicatedMethodCount(30);
        appMetricItem2.setLongMethodCount(7);
        appMetricItem2.setBlockedCodeConductCount(5);
        appMetricItem2.calculateScore();

        AppMetric appMetric = new AppMetric();
        appMetric.addMetricItem(appMetricItem1);
        appMetric.addMetricItem(appMetricItem2);

        Assert.assertEquals(79, appMetric.calculateScore(), 0.01);
    }
}
