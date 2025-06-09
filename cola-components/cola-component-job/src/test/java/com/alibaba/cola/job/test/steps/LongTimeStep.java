package com.alibaba.cola.job.test.steps;

import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;


// This class represents a step that simulates a long execution time of 2 seconds.
public class LongTimeStep extends AbstractStep {
    @Override
    public void doExecute(StepExecution stepExecution) {
        try {
            System.out.println("This is long time executing step, will cost 2 seconds");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doRollback(StepExecution stepExecution) {

    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return false;
    }
}

