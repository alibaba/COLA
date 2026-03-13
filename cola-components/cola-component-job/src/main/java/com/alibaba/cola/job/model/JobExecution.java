package com.alibaba.cola.job.model;

import com.alibaba.cola.job.ExecutionContext;
import com.alibaba.cola.job.repository.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeConverter;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Access(AccessType.FIELD)
public class JobExecution {
    @Id
    @Column(name = "job_id")
    private String jobId;

    // 用来关联批量任务的batchJobId
    @Column(name = "batch_job_id")
    private String batchJobId;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    @Column(name = "execution_context")
    @Convert(converter = ContextConverter.class)
    private ExecutionContext executionContext;

    @Column(name = "execution_status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus executionStatus;

    @Column(name = "message")
    private String message;

    @JsonIgnore
    @Transient
    private List<StepExecution> stepExecutions;

    // JobExecution所从属的job的定义definition
    @JsonIgnore
    @Transient
    private Job jobDef;

    // 已经执行的任务，用于回滚
    @JsonIgnore
    @Transient
    private List<Step> completedSteps = new ArrayList<>();

    public void setStepExecutionList(List<StepExecution> stepExecutions) {
        if (stepExecutions == null) {
            return;
        }
        this.stepExecutions = stepExecutions;
        for (StepExecution stepExecution : this.stepExecutions) {
            stepExecution.setJobExecution(this);
        }
    }

    public boolean isCompleted() {
        return ExecutionStatus.COMPLETED == this.executionStatus;
    }

    public boolean isFailOrRollback() {
        return ExecutionStatus.FAILED == this.executionStatus || ExecutionStatus.isRollback(this.executionStatus);
    }

    @Override
    public String toString() {
        return "JobExecution{" + "jobId='" + jobId + '\'' + ", startTime=" + startTime + ", endTime=" + endTime
                + ", executionStatus=" + executionStatus + '}';
    }

    @JsonIgnore
    public List<Step> getRollbackSteps() {
        List<Step> rollbackSteps = new ArrayList<>(completedSteps);
        Collections.reverse(rollbackSteps);
        return rollbackSteps;
    }
}

class ContextConverter implements AttributeConverter<ExecutionContext, String> {
    @Override
    public String convertToDatabaseColumn(ExecutionContext attribute) {
        return JsonUtil.encode(attribute);
    }

    @Override
    public ExecutionContext convertToEntityAttribute(String dbData) {
        return JsonUtil.decode(dbData, new TypeReference<>() { });
    }
}

