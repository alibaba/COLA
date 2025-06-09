package com.alibaba.cola.job;

import com.alibaba.cola.job.model.*;
import com.alibaba.cola.job.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 批量任务只支持单个Job的异步执行
 */
@Slf4j
public class BatchJobLauncher {
    public static BatchJobExecution execute(BatchJob batchJob) {
        JobRepository jobRepository = batchJob.getJobRepository();
        BatchJobExecution batchJobExecution = new BatchJobExecution(UuidGenerator.nextBatchJobId());
        for (JobInstance jobInstance : batchJob.getJobInstances()) {
            jobInstance.getExecutionContext().setBatchJobId(batchJobExecution.getBatchJobId());
            String jobExecutionId = JobLauncher.executeAsync(jobInstance.getJob(), jobInstance.getExecutionContext(),
                    batchJob.getExecutorService());
            batchJobExecution.addJobExecution(jobExecutionId);
        }
        batchJobExecution.setStartTime(LocalDateTime.now());
        batchJobExecution.setStatus(ExecutionStatus.STARTED);
        batchJobExecution.setJobType(batchJob.getJobType());
        jobRepository.saveBatchJobExecution(batchJobExecution);
        log.info("[BatchJob] started: {}", batchJobExecution);
        return batchJobExecution;
    }

    public static boolean checkAndRefresh(BatchJob batchJob, String batchJobId) {
        JobRepository jobRepository = batchJob.getJobRepository();
        BatchJobExecution batchJobExecution = jobRepository.getBatchJobExecutionByBatchJobId(batchJobId);
        Set<String> jobIds = batchJobExecution.getJobExecutionResults().keySet();
        if (CollectionUtils.isEmpty(jobIds)) {
            return false;
        }
        // 如果已经完成，就不用费时查看每个job的状态了
        if (batchJobExecution.isBatchJobCompleted()) {
            return true;
        }
        for (String jobId : jobIds) {
            JobExecution jobExecution = jobRepository.getJobExecutionByJobId(jobId);
            batchJobExecution.put(jobExecution.getJobId(), jobExecution.getExecutionStatus());
        }
        boolean isChildrenCompleted = batchJobExecution.isChildrenJobsCompleted();
        if (isChildrenCompleted) {
            // 所有子任务都已完成
            batchJobExecution.setStatus(ExecutionStatus.COMPLETED);
            batchJobExecution.setEndTime(LocalDateTime.now());
        } else if (batchJobExecution.isFailed()) {
            batchJobExecution.setStatus(ExecutionStatus.FAILED);
        } else {
            batchJobExecution.setStatus(ExecutionStatus.UNKNOWN);
        }
        jobRepository.updateBatchJobExecution(batchJobExecution);
        return isChildrenCompleted;
    }
}

