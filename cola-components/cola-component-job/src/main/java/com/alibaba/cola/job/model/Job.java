package com.alibaba.cola.job.model;


import com.alibaba.cola.job.repository.JobRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务的定义，主要定义了任务所包含的steps，以及相关metadata，比如任务形态（同步，异步），是否需要回滚。
 */
@Slf4j
@Data
public class Job {

    private String name;

    private List<Step> steps = new ArrayList<>();

    private JobRepository jobRepository;

    // 任务是否需要回滚
    private boolean needRollback;

    // 任务是否是异步任务，主要影响任务的状态设置
    private boolean isAsync;

    public void execute(JobExecution jobExecution) {
        if (ExecutionStatus.isRollback(jobExecution.getExecutionStatus())) {
            rollback(jobExecution);
        } else {
            run(jobExecution);
        }
    }

    private void run(JobExecution jobExecution) {
        jobExecution.setExecutionStatus(ExecutionStatus.STARTED);
        jobExecution.setStartTime(LocalDateTime.now());
        log.info("[Job - {}] started: {}", jobExecution.getJobId(), jobExecution);
        this.jobRepository.updateJobExecution(jobExecution);
        try {
            for (Step step : this.getSteps()) {
                runStep(step, jobExecution);
                jobExecution.getCompletedSteps().add(step);
            }
            if (isAsync) {
                // 对于异步任务，触发完所有的steps，状态仍然是running
                jobExecution.setExecutionStatus(ExecutionStatus.RUNNING);
            } else {
                // 对于同步任务，执行完所有的steps，不出问题的话，任务就Completed了
                jobExecution.setEndTime(LocalDateTime.now());
                jobExecution.setExecutionStatus(ExecutionStatus.COMPLETED);
            }

        } catch (Exception e) {
            if (this.isNeedRollback()) {
                onFailToRollback(e, jobExecution);
            } else {
                onException(e, jobExecution, ExecutionStatus.FAILED);
            }
        } finally {
            onFinally(jobExecution);
        }
    }

    private void onFailToRollback(Exception e, JobExecution jobExecution) {
        log.error("[Job - {}] execute error, need to rollback, error: {}", jobExecution.getJobId(), e.getMessage(), e);
        jobExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_STARTED);
        try {
            List<Step> rollbackSteps = jobExecution.getRollbackSteps();
            log.info("[Job - {}] start rollback: {}", jobExecution.getJobId(),
                    rollbackSteps.stream().map(s -> s.getClass().getSimpleName()).collect(Collectors.toList()));
            rollbackSteps.forEach(s -> rollbackStep(s, jobExecution));
        } catch (Exception ee) {
            log.error("[Job - {}] rollback error: ", jobExecution.getJobId(), ee);
            jobExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_FAILED);
            return;
        }
        jobExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_COMPLETED);
    }

    private void rollbackStep(Step step, JobExecution jobExecution) {
        StepExecution stepExecution = this.jobRepository.getStepExecution(jobExecution.getJobId(),
                step.getClass().getSimpleName());

        if (stepExecution.isRollbacked()) {
            log.info("[Job - {}][Step - {}] rollback skip", jobExecution.getJobId(), stepExecution.getStepName());
        } else {
            if (step.needRollBack(stepExecution)) {
                // 因为stepExecution是持久层取出的，所以要重新设置step和jobExecution
                stepExecution.setStep(step);
                stepExecution.setJobExecution(jobExecution);
                step.rollback(stepExecution);
            } else {
                log.info("[Job - {}][Step - {}] no need rollback", jobExecution.getJobId(),
                        stepExecution.getStepName());
            }
        }
    }

    private void runStep(Step step, JobExecution jobExecution) {
        StepExecution stepExecution = this.jobRepository.getStepExecution(jobExecution.getJobId(),
                step.getClass().getSimpleName());
        // 全新的step
        if (stepExecution == null) {
            stepExecution = new StepExecution(step);
            stepExecution.setJobExecution(jobExecution);
            stepExecution.setExecutionStatus(ExecutionStatus.STARTED);
            stepExecution.setStartTime(LocalDateTime.now());
            this.jobRepository.saveStepExecution(stepExecution);
            step.execute(stepExecution);
            return;
        }
        // step已经执行成功，跳过
        if (stepExecution.isCompleted()) {
            log.info("[Job - {}][Step - {}] skip", jobExecution.getJobId(), stepExecution.getStepName());
        } else {
            // 因为stepExecution是持久层取出的，所以要重新设置step和jobExecution
            stepExecution.setStep(step);
            stepExecution.setJobExecution(jobExecution);
            stepExecution.setExecutionStatus(ExecutionStatus.STARTED);
            this.jobRepository.updateStepExecution(stepExecution);
            step.execute(stepExecution);
        }
    }

    private void rollback(JobExecution jobExecution) {
        jobExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_STARTED);
        jobExecution.setStartTime(LocalDateTime.now());
        log.info("[Job - {}] rollback started: {}", jobExecution.getJobId(), jobExecution);
        this.jobRepository.updateJobExecution(jobExecution);
        try {
            for (Step step : getReverseSteps()) {
                StepExecution stepExecution = this.jobRepository.getStepExecution(jobExecution.getJobId(),
                        step.getClass().getSimpleName());
                if (stepExecution == null) {
                    continue;
                }
                // step已经执行成功，跳过
                if (stepExecution.isRollbacked()) {
                    log.info("[Job - {}][Step - {}] rollback skip", jobExecution.getJobId(),
                            stepExecution.getStepName());
                } else {
                    stepExecution.setStep(step);
                    stepExecution.setJobExecution(jobExecution);
                    stepExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_STARTED);
                    this.jobRepository.updateStepExecution(stepExecution);
                    step.rollback(stepExecution);
                }
            }
            jobExecution.setExecutionStatus(ExecutionStatus.ROLLBACK_COMPLETED);
        } catch (Exception e) {
            onException(e, jobExecution, ExecutionStatus.ROLLBACK_FAILED);
        } finally {
            onFinally(jobExecution);
        }
    }

    public List<Step> getReverseSteps() {
        List<Step> reverseSteps = new ArrayList<>(this.steps);
        Collections.reverse(reverseSteps);
        return reverseSteps;
    }

    private void onException(Exception e, JobExecution jobExecution, ExecutionStatus status) {
        log.error("[Job - {}] execute error: {}", jobExecution.getJobId(), e.getMessage(), e);
        jobExecution.setMessage(e.getMessage());
        jobExecution.setEndTime(LocalDateTime.now());
        jobExecution.setExecutionStatus(status);
    }

    private void onFinally(JobExecution jobExecution) {
        jobExecution.setUpdateTime(LocalDateTime.now());
        log.info("[Job - {}] end, status is [{}]", jobExecution.getJobId(), jobExecution.getExecutionStatus());
        this.jobRepository.updateJobExecution(jobExecution);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }

        Job job = (Job) o;

        if (isNeedRollback() != job.isNeedRollback()) {
            return false;
        }
        return getName() != null ? getName().equals(job.getName()) : job.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isNeedRollback() ? 1 : 0);
        return result;
    }
}
