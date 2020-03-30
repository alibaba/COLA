package com.alibaba.craftsman.domain.metrics.devquality;

import com.alibaba.craftsman.domain.metrics.MetricItem;
import lombok.Data;

@Data
public class BugMetricItem extends MetricItem{

    private int bugCount; //缺陷数量
    private long checkInCodeCount; //check in的代码量

    private static int DEFAULT_SCORE = 100;
    private static double STEP_SIZE = 1;
    private static double STEP_MINUS_SCORE = 5;

    public BugMetricItem(int bugCount, long checkInCodeCount){
        this.bugCount = bugCount;
        this.checkInCodeCount = checkInCodeCount;
    }

    @Override
    public double calculateScore() {
        double score = DEFAULT_SCORE;
        //千行代码缺陷数
        double bugPerThousandLinesCode = bugCount * 1000 / checkInCodeCount;
        for(double counter = STEP_SIZE; counter <= bugPerThousandLinesCode; counter = counter + STEP_SIZE){
            score = score - STEP_MINUS_SCORE;
        }
        return score;
    }
}
