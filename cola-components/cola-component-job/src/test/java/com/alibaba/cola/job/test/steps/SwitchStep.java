package com.alibaba.cola.job.test.steps;

import com.alibaba.cola.job.model.AbstractStep;
import com.alibaba.cola.job.model.StepExecution;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwitchStep extends AbstractStep {
    private static boolean switcher = true;

    @Override
    public void doExecute(StepExecution stepExecution) {
        if(switcher){
            switcher = false;
            throw new RuntimeException("something wrong, rerun will fix it");
        }
        log.info("flipped back to normal");
        switcher = true;
    }

    @Override
    public void doRollback(StepExecution stepExecution) {

    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return false;
    }
}

