package com.alibaba.craftsman.domain;

import com.alibaba.craftsman.domain.metrics.devquality.BugMetricItem;
import org.junit.Assert;
import org.junit.Test;

public class BugMetricTest {

    @Test
    public void test5BugsPer1000LinesCode(){
        BugMetricItem bugMetricItem = new BugMetricItem(5, 1000);
        Assert.assertEquals(75, bugMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void test2BugsPer1000LinesCode(){
        BugMetricItem bugMetricItem = new BugMetricItem(2, 1000);
        Assert.assertEquals(90, bugMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void test5BugsPer10000LinesCode(){
        BugMetricItem bugMetricItem = new BugMetricItem(5, 10000);
        Assert.assertEquals(100, bugMetricItem.calculateScore(), 0.01);
    }
}
