package com.alibaba.cola.job.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
public abstract class AbstractStep implements Step {

    private Job job;

    @Override
    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public Job getJob() {
        return this.job;
    }

    @Override
    public void execute(StepExecution stepExecution) {
        try {
            logInfo(stepExecution, "start");
            this.doExecute(stepExecution);
            stepExecution.setExecutionStatus(ExecutionStatus.COMPLETED);
        } catch (Exception e) {
            log.error("[job - {}][Step - {}] need rollback: {},execute error: {}",
                    stepExecution.getJobExecution().getJobId(), stepExecution.getStepName(),
                    this.needRollBack(stepExecution), e.getMessage());
            stepExecution.setMessage("[execute error]: " + e.getMessage() + "; ");
            if (this.needRollBack(stepExecution)) {
                this.internalRollback(stepExecution);
            } else {
                stepExecution.setExecutionStatus(ExecutionStatus.FAILED);
            }
            throw e;
        } finally {
            updateStepExecution(stepExecution);
        }
    }

    @Override
    public void rollback(StepExecution stepExecution) {
        try {
            internalRollback(stepExecution);
        } finally {
            updateStepExecution(stepExecution);
        }
    }

    private void updateStepExecution(StepExecution stepExecution) {
        logInfo(stepExecution, "end, status is [" + stepExecution.getExecutionStatus()+"]");
        stepExecution.setEndTime(LocalDateTime.now());
        stepExecution.getRepository().updateStepExecution(stepExecution);
    }

    private void internalRollback(StepExecution stepExecution) {
        try {
            logInfo(stepExecution, "rollback start");
            stepExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_STARTED);
            this.doRollback(stepExecution);
            stepExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_COMPLETED);
            logInfo(stepExecution, "rollback end");
        } catch (Exception e) {
            String errMsg = String.format("[Job - %s][Step - %s]Rollback with error: %s",
                    stepExecution.getJobExecution().getJobId(), stepExecution.getStepName(), e.getMessage());
            log.error(errMsg, e);
            stepExecution.setMessage(stepExecution.getMessage() + "[rollback error]: " + e.getMessage());
            stepExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_FAILED);
            throw e;
        }
    }

    public abstract void doExecute(StepExecution stepExecution);

    public void doRollback(StepExecution stepExecution) {
        log.info("[job - {}][Step - {}] The rollback content is empty.", stepExecution.getJobExecution().getJobId(),
                stepExecution.getStepName());
    }

    @Override
    public boolean needRollBack(StepExecution stepExecution) {
        return false;
    }

    private void logInfo(StepExecution stepExecution, String message) {
        log.info("[Job - {}][Step - {}] {}", stepExecution.getJobExecution().getJobId(), stepExecution.getStepName(),
                message);
    }
}

