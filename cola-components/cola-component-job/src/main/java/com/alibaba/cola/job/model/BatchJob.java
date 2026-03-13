package com.alibaba.cola.job.model;

import com.alibaba.cola.job.repository.JobRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 一个批量任务，包含了多个JobInstance
 */
public class BatchJob {
    @Getter
    private final List<JobInstance> jobInstances;

    @Getter
    private JobRepository jobRepository;

    @Getter
    @Setter
    private String jobType;

    @Getter
    private ExecutorService executorService;

    public BatchJob() {
        jobInstances = new ArrayList<>();
    }

    public BatchJob jobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
        return this;
    }

    public BatchJob executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public BatchJob add(JobInstance jobInstance) {
        jobInstances.add(jobInstance);
        return this;
    }
}

