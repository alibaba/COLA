package com.alibaba.cola.job.repository;


import com.alibaba.cola.job.model.BatchJobExecution;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.model.StepExecution;

import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository {
    BatchJobExecution saveBatchJobExecution(BatchJobExecution batchJobExecution);

    void updateBatchJobExecution(BatchJobExecution batchJobExecution);

    BatchJobExecution getBatchJobExecutionByBatchJobId(String batchJobId);

    JobExecution saveJobExecution(JobExecution jobExecution);

    void updateJobExecution(JobExecution jobExecution);

    JobExecution getJobExecutionByJobId(String jobId);

    void saveStepExecution(StepExecution stepExecution);

    void updateStepExecution(StepExecution stepExecution);

    StepExecution getStepExecution(String jobExecutionId, String stepName);

    List<StepExecution> listStepExecutions(String jobExecutionId);

    List<BatchJobExecution> findNotCompletedBatchJobsOlderThan(LocalDateTime thresholdTime);
}
