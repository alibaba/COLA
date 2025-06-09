package com.alibaba.cola.job.repository.memory;


import com.alibaba.cola.job.JobException;
import com.alibaba.cola.job.repository.AbstractJobRepository;
import com.alibaba.cola.job.model.BatchJobExecution;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.model.StepExecution;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存任务仓储，没有落盘持久化，适合Demo和一次性的任务
 */
public class MemoryJobRepository extends AbstractJobRepository {

    // key是batchJobId
    private Map<String, BatchJobExecution> batchJobMap = new ConcurrentHashMap<>();

    // key是jobExecutionId
    private Map<String, JobExecution> jobMap = new ConcurrentHashMap<>();

    // key是jobExecutionId+stepName
    private Map<String, StepExecution> stepMap = new ConcurrentHashMap<>();

    // key是jobExecutionId
    private Map<String, Set<StepExecution>> stepSetMap = new ConcurrentHashMap<>();

    @Override
    public JobExecution getJobExecutionByJobId(String jobId) {
        return jobMap.get(jobId);
    }

    @Override
    public BatchJobExecution saveBatchJobExecution(BatchJobExecution batchJobExecution) {
        batchJobMap.put(batchJobExecution.getBatchJobId(), batchJobExecution);
        return batchJobExecution;
    }

    @Override
    public void updateBatchJobExecution(BatchJobExecution batchJobExecution) {
        batchJobMap.put(batchJobExecution.getBatchJobId(), batchJobExecution);
    }

    @Override
    public BatchJobExecution getBatchJobExecutionByBatchJobId(String batchJobId) {
        return batchJobMap.get(batchJobId);
    }

    @Override
    public JobExecution saveJobExecution(JobExecution jobExecution) {
        Assert.notNull(jobExecution, "jobExecution cannot be null");
        Assert.notNull(jobExecution.getJobDef(), "job cannot be null.");
        Assert.notNull(jobExecution.getJobId(), "jobExecutionId cannot be null.");
        jobMap.put(jobExecution.getJobId(), jobExecution);
        return jobExecution;
    }

    @Override
    public void updateJobExecution(JobExecution jobExecution) {
        Assert.notNull(jobExecution, "jobExecution cannot be null");
        String executionId = jobExecution.getJobId();
        Assert.notNull(executionId, "jobExecutionId cannot be null.");
        JobExecution oldJobExecution = jobMap.get(executionId);
        if (oldJobExecution == null) {
            throw new JobException("No jobExecution with id:" + executionId + " found");
        }
        jobMap.put(executionId, jobExecution);
    }

    @Override
    public void saveStepExecution(StepExecution stepExecution) {
        Assert.notNull(stepExecution.getJobExecution(), "jobExecution cannot be null");
        String jobExecutionId = stepExecution.getJobExecution().getJobId();
        String stepName = stepExecution.getStepName();
        stepMap.put(jobExecutionId + stepName, stepExecution);
        Set<StepExecution> stepSet = stepSetMap.get(jobExecutionId);
        if (stepSet == null) {
            stepSet = new HashSet<>();
            stepSetMap.put(jobExecutionId, stepSet);
        }
        stepSet.add(stepExecution);
    }

    @Override
    public void updateStepExecution(StepExecution stepExecution) {
        Assert.notNull(stepExecution.getJobExecution(), "jobExecution cannot be null");
        String jobExecutionId = stepExecution.getJobExecution().getJobId();
        String stepName = stepExecution.getStepName();
        stepMap.put(jobExecutionId + stepName, stepExecution);
        Set<StepExecution> stepSet = stepSetMap.get(jobExecutionId);
        for (StepExecution target : stepSet) {
            if (target.equals(stepExecution)) {
                BeanUtils.copyProperties(stepExecution, target);
            }
        }
    }

    @Override
    public StepExecution getStepExecution(String jobExecutionId, String stepName) {
        return stepMap.get(jobExecutionId + stepName);
    }

    @Override
    public List<StepExecution> listStepExecutions(String jobExecutionId) {
        return stepSetMap.get(jobExecutionId).stream().toList();
    }

}

