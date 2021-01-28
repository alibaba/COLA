package com.alibaba.craftsman.domain.metrics;


import com.alibaba.cola.domain.EntityObject;
import com.alibaba.craftsman.domain.user.UserProfile;
import lombok.Getter;
import lombok.Setter;

/**
 * Metric
 * 指标
 * @author Frank Zhang
 * @date 2018-07-04 1:23 PM
 */
public abstract class Metric extends EntityObject implements Measurable{

    private double score;

    @Getter
    @Setter
    protected UserProfile metricOwner;

    public Metric(){

    }

    public Metric(UserProfile metricOwner){
        this.metricOwner = metricOwner;
    }


    /**
     * 度量名称，用于UI显示
     * @return
     */
    abstract public String getName();

    /**
     * 度量Code，用于数据库存储
     * @return
     */
    abstract public String getCode();

    abstract public double getWeight();

    @Override
    public String toString(){
        return this.getName() + " " + this.score;
    }

}
