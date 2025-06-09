package com.alibaba.cola.job.test.steps;


import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;

public class MyStep1 extends AbstractStep {

    @Override
    public void doRollback(StepExecution stepExecution) {

    }

    @Override
    public void doExecute(StepExecution stepExecution) {
        System.out.println("this is step1");
        stepExecution.getExecutionContext().putString("step1", "something need pass to step2");
    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return true;
    }
}

