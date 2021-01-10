package com.alibaba.craftsman.domain.metrics.techinfluence;

import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.craftsman.domain.metrics.MetricItem;
import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * ATAMetricItem
 * 线上分享，ATA文章指标项
 * @author Frank Zhang
 * @date 2018-07-04 3:20 PM
 */
@Data
public class ATAMetricItem extends MetricItem {

    private static Logger logger = LoggerFactory.getLogger(ATAMetricItem.class);

    private String authorId;//作者
    private String title;//文章标题
    private String url;//文章链接
    private long thumbsUpCount;//点赞数
    private long hitCount;//点击数
    private long commentCount;//评论数
    private long favoriteCount;//收藏数

    private static int HIT_STEP_SIZE = 100;
    private static int THUMB_UPS_STEP_SIZE = 20;
    private static int FAVORITE_STEP_SIZE = 15;
    private static int COMMENT_STEP_SIZE = 3;
    private static double STEP_SCORE = 0.25;
    private static double BASIC_SCORE = 0.5;


    public ATAMetricItem(){

    }

    public ATAMetricItem(String title, long thumbsUpCount, long hitCount, long favoriteCount, long commentCount) {
        this.title = title;
        this.thumbsUpCount = thumbsUpCount;
        this.hitCount = hitCount;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
    }

    public static ATAMetricItem valueOf(String json){
        return JSON.parseObject(json, ATAMetricItem.class);
    }

    @Override
    public double calculateScore() {
        logger.debug("calculate score for : " + this);
        double score = BASIC_SCORE;
        score = addScoreByHitCount(score);
        score = addScoreByThumbsupCount(score);
        score = addScoreByFavoriteCount(score);
        score = addScoreByCommentCount(score);
        logger.debug("calculated score is : " + score);
        return score;
    }

    private double addScoreByHitCount(double score) {
        for(int counter = HIT_STEP_SIZE; counter <= hitCount; counter = counter + HIT_STEP_SIZE){
            score = score + STEP_SCORE;
        }
        return score;
    }

    private double addScoreByThumbsupCount(double score){
        for(int counter = THUMB_UPS_STEP_SIZE; counter <= thumbsUpCount; counter = counter + THUMB_UPS_STEP_SIZE){
            score = score + STEP_SCORE;
        }
        return score;
    }

    private double addScoreByFavoriteCount(double score){
        for(int counter = FAVORITE_STEP_SIZE; counter <= favoriteCount; counter = counter + FAVORITE_STEP_SIZE){
            score = score + STEP_SCORE;
        }
        return score;
    }

    private double addScoreByCommentCount(double score){
        for(int counter = COMMENT_STEP_SIZE; counter <= commentCount; counter = counter + COMMENT_STEP_SIZE){
            score = score + STEP_SCORE;
        }
        return score;
    }

}
