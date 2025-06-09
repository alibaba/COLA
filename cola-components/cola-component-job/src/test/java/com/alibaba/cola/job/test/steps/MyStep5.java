package com.alibaba.cola.job.test.steps;


import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;
import lombok.Setter;

public class MyStep5 extends AbstractStep {

    @Setter
    private boolean isRollbackThrow = true;

    @Override
    public void doRollback(StepExecution stepExecution) {
        System.out.println("this is step5 rollback");
        if(isRollbackThrow){
            throw new RuntimeException("oops, rollback failed");
        }
    }

    @Override
    public void doExecute(StepExecution stepExecution) {
        System.out.println("this is step5");
    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return true;
    }
}


