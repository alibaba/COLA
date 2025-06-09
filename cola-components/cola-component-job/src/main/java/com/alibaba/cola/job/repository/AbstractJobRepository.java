package com.alibaba.cola.job.repository;

import com.alibaba.cola.job.model.BatchJobExecution;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractJobRepository implements JobRepository {
    @Override
    public List<BatchJobExecution> findNotCompletedBatchJobsOlderThan(LocalDateTime thresholdTime) {
        return null;
    }
}
