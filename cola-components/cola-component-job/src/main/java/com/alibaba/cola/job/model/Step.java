package com.alibaba.cola.job.model;

public interface Step {

    void execute(StepExecution stepExecution);

    void rollback(StepExecution stepExecution);

    boolean needRollBack(StepExecution stepExecution);

    void setJob(Job job);

    Job getJob();
}
