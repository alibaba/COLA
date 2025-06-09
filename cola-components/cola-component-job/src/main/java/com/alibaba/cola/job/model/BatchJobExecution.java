package com.alibaba.cola.job.model;


import com.alibaba.cola.job.repository.JsonUtil;
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
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Access(AccessType.FIELD)
public class BatchJobExecution {
    @Id
    @Column(name = "batch_job_id")
    private String batchJobId;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "execution_status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(name = "job_execution_results")
    @Convert(converter = ResultsConverter.class)
    // key: jobExecutionId, value: 执行的结果状态
    private Map<String, ExecutionStatus> jobExecutionResults = new HashMap<>();

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    public BatchJobExecution() {
    }

    public BatchJobExecution(String batchJobId) {
        this.batchJobId = batchJobId;
    }

    public void addJobExecution(String jobExecutionId) {
        jobExecutionResults.put(jobExecutionId, ExecutionStatus.STARTED);
    }

    public void put(String jobExecutionId, ExecutionStatus executionStatus) {
        jobExecutionResults.put(jobExecutionId, executionStatus);
    }

    public boolean isCompleted() {
        if (status == ExecutionStatus.COMPLETED) {
            return true;
        }
        return isAllJobsCompleted();
    }

    public boolean isBatchJobCompleted() {
        return status == ExecutionStatus.COMPLETED;
    }

    public boolean isChildrenJobsCompleted() {
        for (ExecutionStatus jobStatus : jobExecutionResults.values()) {
            if (jobStatus != ExecutionStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllJobsCompleted() {
        for (ExecutionStatus jobStatus : jobExecutionResults.values()) {
            if (jobStatus != ExecutionStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查batchJob中是否所有的job都已经达到终态
     * 终态的定义是：Completed，Failed or Rollback
     * @return
     */
    public boolean isTerminated(){
        for (ExecutionStatus jobStatus : jobExecutionResults.values()) {
            if (!ExecutionStatus.isTerminated(jobStatus)) {
                return false;
            }
        }
        return true;
    }

    public boolean isFailed() {
        for (ExecutionStatus jobStatus : jobExecutionResults.values()) {
            if (jobStatus == ExecutionStatus.FAILED) {
                return true;
            }
        }
        return false;
    }

}

class ResultsConverter implements AttributeConverter<Map<String, ExecutionStatus>, String> {
    @Override
    public String convertToDatabaseColumn(Map attribute) {
        return JsonUtil.encode(attribute);
    }

    @Override
    public Map<String, ExecutionStatus> convertToEntityAttribute(String dbData) {
        return JsonUtil.decode(dbData, new TypeReference<>() { });
    }
}
