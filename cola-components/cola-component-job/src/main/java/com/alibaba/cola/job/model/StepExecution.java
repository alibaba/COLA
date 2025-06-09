package com.alibaba.cola.job.model;


import com.alibaba.cola.job.ExecutionContext;
import com.alibaba.cola.job.repository.JobRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Data
@Entity
@Access(AccessType.FIELD)
public class StepExecution {
    @Id
    @Column(name = "step_id")
    // stepId = jobId + "-" +stepName
    private String stepId;

    // StepExecution的Step定义
    @JsonIgnore
    @Transient
    private Step step;

    // 该stepExecution所归属的jobExecution
    @JsonIgnore
    @Transient
    private JobExecution jobExecution;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "step_name")
    private String stepName;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @Column(name = "execution_status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus executionStatus;

    @Column(name = "execution_context")
    @Convert(converter = ContextConverter.class)
    private ExecutionContext executionContext;

    @Column(name = "message")
    private String message;

    public boolean isCompleted() {
        return this.executionStatus == ExecutionStatus.COMPLETED;
    }
    public boolean isRollbacked() {
        return this.executionStatus == ExecutionStatus.ROLLBACK_COMPLETED;
    }

    public StepExecution(){
    }

    public StepExecution(Step step) {
        this.step = step;
        this.stepName = step.getClass().getSimpleName();
    }

    public void setJobExecution(JobExecution jobExecution) {
        Assert.notNull(jobExecution, "jobExecution cannot be null");
        this.jobExecution = jobExecution;
        this.jobId = jobExecution.getJobId();
        this.stepId = this.jobId + "-" + this.stepName;
    }

    public ExecutionContext getExecutionContext() {
        if (executionContext == null) {
            executionContext = this.jobExecution.getExecutionContext();
        }
        return executionContext;
    }

    @JsonIgnore
    public JobRepository getRepository() {
        Assert.notNull(jobExecution, "jobExecution cannot be null");
        Assert.notNull(jobExecution.getJobDef(), "job must be attached to jobExecution");
        return this.jobExecution.getJobDef().getJobRepository();
    }

    @Override
    public String toString() {
        return "StepExecution{" + "stepExecutionId='" + stepId + '\'' + ", executionStatus=" + executionStatus + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StepExecution)) {
            return false;
        }
        StepExecution that = (StepExecution) o;
        return stepId != null
                ? stepId.equals(that.getStepId())
                : that.getStepId() == null;
    }

    @Override
    public int hashCode() {
        return getJobExecution() != null ? stepId.hashCode() : 0;
    }
}
