package com.alibaba.cola.job.test.steps;

import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;

public class MyStep4 extends AbstractStep {

    @Override
    public void doRollback(StepExecution stepExecution) {
        System.out.println("this is step4 rollback");
        throw new RuntimeException("oops, rollback failed");
    }

    @Override
    public void doExecute(StepExecution stepExecution) {
        System.out.println("this is step4");
        throw new RuntimeException("something wrong happened");
    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return true;
    }
}


