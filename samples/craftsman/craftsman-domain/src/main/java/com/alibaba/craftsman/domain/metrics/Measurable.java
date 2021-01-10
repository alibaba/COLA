package com.alibaba.craftsman.domain.metrics;

import java.io.Serializable;

/**
 * Measurable
 * 可度量的
 * @author Frank Zhang
 * @date 2018-07-04 1:32 PM
 */
public interface Measurable extends Serializable{

    /**
     * 计算分数
     * @return
     */
    public double calculateScore();
}
