package com.alibaba.cola.job.test.steps;

import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;

// This class is used to simulate a step that fails during execution
public class FailedStep extends AbstractStep {
    @Override
    public void doExecute(StepExecution stepExecution) {
        System.out.println("this is FailedStep");
        throw new RuntimeException("something wrong happened");
    }

    @Override
    public void doRollback(StepExecution stepExecution) {

    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return false;
    }
}

