package com.alibaba.cola.job.repository.redis;


import com.alibaba.cola.job.model.BatchJobExecution;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.model.StepExecution;
import com.alibaba.cola.job.repository.AbstractJobRepository;
import com.alibaba.cola.job.repository.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class RedisJobRepository extends AbstractJobRepository {

    @Resource
    private RedisTemplate<String, JobExecution> jobRedisTemplate;

    @Resource
    private RedisTemplate<String, StepExecution> stepRedisTemplate;

    @Resource
    private RedisTemplate<String, BatchJobExecution> batchJobRedisTemplate;


    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = JsonUtil.ObjectMapperFactory.getObjectMapper();
    }

    public static final String JOB_PREFIX = "job:";

    public static final String BATCH_JOB_PREFIX = "batchJob:";

    @Override
    public BatchJobExecution saveBatchJobExecution(BatchJobExecution batchJobExecution) {
        String key = BATCH_JOB_PREFIX + batchJobExecution.getBatchJobId();
        batchJobRedisTemplate.opsForValue().set(key, batchJobExecution);
        return batchJobExecution;
    }

    @Override
    public void updateBatchJobExecution(BatchJobExecution batchJobExecution) {
        String key = BATCH_JOB_PREFIX + batchJobExecution.getBatchJobId();
        batchJobRedisTemplate.opsForValue().set(key, batchJobExecution);
    }

    @Override
    public BatchJobExecution getBatchJobExecutionByBatchJobId(String batchJobId) {
        String key = BATCH_JOB_PREFIX + batchJobId;
        return objectMapper.convertValue(batchJobRedisTemplate.opsForValue().get(key),
                new TypeReference<BatchJobExecution>() { });
    }

    @Override
    public JobExecution saveJobExecution(JobExecution jobExecution) {
        String key = JOB_PREFIX + jobExecution.getJobId();
        jobRedisTemplate.opsForValue().set(key, jobExecution);
        return jobExecution;
    }

    @Override
    public void updateJobExecution(JobExecution jobExecution) {
        String key = JOB_PREFIX + jobExecution.getJobId();
        jobRedisTemplate.opsForValue().set(key, jobExecution);
    }

    @Override
    public JobExecution getJobExecutionByJobId(String jobId) {
        String key = JOB_PREFIX + jobId;
        return objectMapper.convertValue(jobRedisTemplate.opsForValue().get(key),
                new TypeReference<JobExecution>() { });
    }
    @Override
    public void saveStepExecution(StepExecution stepExecution) {
        String key = JOB_PREFIX + stepExecution.getJobExecution().getJobId() + ":" + stepExecution.getStepName();
        stepRedisTemplate.opsForValue().set(key, stepExecution);
    }

    @Override
    public void updateStepExecution(StepExecution stepExecution) {
        String key = JOB_PREFIX + stepExecution.getJobExecution().getJobId() + ":" + stepExecution.getStepName();
        stepRedisTemplate.opsForValue().set(key, stepExecution);
    }

    @Override
    public StepExecution getStepExecution(String jobId, String stepName) {
        String key = JOB_PREFIX + jobId + ":" + stepName;
        return objectMapper.convertValue(stepRedisTemplate.opsForValue().get(key),
                new TypeReference<StepExecution>() { });
    }

    @Override
    public List<StepExecution> listStepExecutions(String jobExecutionId) {
        List<StepExecution> stepExecutions = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions().match(JOB_PREFIX + jobExecutionId + ":*").count(10) // 每次返回的数量
                .build();

        Cursor<String> cursor = jobRedisTemplate.scan(options);
        while (cursor.hasNext()) {
            StepExecution stepExecution = objectMapper.convertValue(stepRedisTemplate.opsForValue().get(cursor.next()),
                    new TypeReference<StepExecution>() { });
            stepExecutions.add(stepExecution);
        }
        return stepExecutions;
    }

}

