package com.alibaba.craftsman.domain;

import com.alibaba.craftsman.domain.metrics.techinfluence.ATAMetric;
import com.alibaba.craftsman.domain.metrics.techinfluence.ATAMetricItem;
import com.alibaba.craftsman.domain.metrics.techinfluence.InfluenceMetric;
import com.alibaba.craftsman.domain.user.UserProfile;
import org.junit.Assert;
import org.junit.Test;

/**
 * ATAMetricTest
 *
 * @author Frank Zhang
 * @date 2019-02-26 2:07 PM
 */
public class ATAMetricTest {

    @Test
    public void testBasicScore(){
        ATAMetricItem ataMetricItem = new ATAMetricItem("article",19,99,14,2) ;
        Assert.assertEquals(0.5, ataMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void testNormalScore(){
        ATAMetricItem ataMetricItem = new ATAMetricItem("article",20,100,15,3) ;
        Assert.assertEquals(1.5, ataMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void testPopularScore(){
        ATAMetricItem ataMetricItem = new ATAMetricItem("article",100, 500, 75, 15) ;
        Assert.assertEquals(5.5, ataMetricItem.calculateScore(), 0.01);
    }

    @Test
    public void testJSON(){
        ATAMetricItem ataMetricItem = new ATAMetricItem();
        ataMetricItem.setTitle("title");
        ataMetricItem.setUrl("sharingLink");
        ataMetricItem.setFavoriteCount(1000);
        ataMetricItem.setCommentCount(203);
        ataMetricItem.setSubMetric(new ATAMetric(new InfluenceMetric(new UserProfile("78492"))));

        String jsonStr = ataMetricItem.toJsonString();
        ATAMetricItem jsonObject = ATAMetricItem.valueOf(jsonStr);

        Assert.assertEquals(ataMetricItem.getTitle(), jsonObject.getTitle());
        Assert.assertEquals(ataMetricItem.getUrl(), jsonObject.getUrl());
        Assert.assertEquals(ataMetricItem.getFavoriteCount(), jsonObject.getFavoriteCount());
        Assert.assertEquals(ataMetricItem.getCommentCount(), jsonObject.getCommentCount());
        Assert.assertEquals(ataMetricItem.getHitCount(), jsonObject.getHitCount());
        Assert.assertEquals(ataMetricItem, jsonObject);
    }

    @Test
    public void testATAMetric(){
        ATAMetric ataMetric = new ATAMetric(new InfluenceMetric(new UserProfile()));
        ataMetric.addMetricItem( new ATAMetricItem("article",19,99,14,2));
        ataMetric.addMetricItem( new ATAMetricItem("article",20,100,15,3) );
        ataMetric.addMetricItem( new ATAMetricItem("article",100, 500, 75, 15) );
        Assert.assertEquals(7.5, ataMetric.calculateScore(), 0.01);
    }
}
