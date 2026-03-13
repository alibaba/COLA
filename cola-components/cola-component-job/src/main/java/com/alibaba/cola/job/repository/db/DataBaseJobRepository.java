package com.alibaba.cola.job.repository.db;


import com.alibaba.cola.job.model.BatchJobExecution;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.model.StepExecution;
import com.alibaba.cola.job.repository.AbstractJobRepository;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;

public class DataBaseJobRepository extends AbstractJobRepository {

    @Resource
    private BatchJobExecutionRepository batchJobExecutionRepository;

    @Resource
    private JobExecutionRepository jobExecutionRepository;

    @Resource
    private StepExecutionRepository stepExecutionRepository;

    @Override
    public JobExecution getJobExecutionByJobId(String jobId) {
        return jobExecutionRepository.getByJobId(jobId);
    }

    @Override
    public BatchJobExecution saveBatchJobExecution(BatchJobExecution batchJobExecution) {
        return batchJobExecutionRepository.save(batchJobExecution);
    }

    @Override
    public void updateBatchJobExecution(BatchJobExecution batchJobExecution) {
        batchJobExecutionRepository.save(batchJobExecution);
    }

    @Override
    public BatchJobExecution getBatchJobExecutionByBatchJobId(String batchJobId) {
        return batchJobExecutionRepository.getByBatchJobId(batchJobId);
    }

    @Override
    public JobExecution saveJobExecution(JobExecution jobExecution) {
        return jobExecutionRepository.save(jobExecution);
    }

    @Override
    public void updateJobExecution(JobExecution jobExecution) {
        jobExecutionRepository.save(jobExecution);
    }

    @Override
    public void saveStepExecution(StepExecution stepExecution) {
        stepExecutionRepository.save(stepExecution);
    }

    @Override
    public void updateStepExecution(StepExecution stepExecution) {
        stepExecutionRepository.save(stepExecution);
    }

    @Override
    public StepExecution getStepExecution(String jobId, String stepName) {
        return stepExecutionRepository.getByJobIdAndStepName(jobId, stepName);
    }

    @Override
    public List<StepExecution> listStepExecutions(String jobId) {
        return stepExecutionRepository.findByJobId(jobId);
    }

    @Override
    public List<BatchJobExecution> findNotCompletedBatchJobsOlderThan(LocalDateTime thresholdTime) {
        return batchJobExecutionRepository.findNotCompletedBatchJobsOlderThan(thresholdTime);
    }
}

