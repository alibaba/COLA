package com.alibaba.cola.job.test.steps;


import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;

public class MyStep2 extends AbstractStep {

    @Override
    public void doRollback(StepExecution stepExecution) {

    }

    @Override
    public void doExecute(StepExecution stepExecution) {
        System.out.println("this is step2, information from step1: " + stepExecution.getExecutionContext().getString("step1"));
        stepExecution.getExecutionContext().putString("step2", "valuable information to next");
    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return true;
    }
}

