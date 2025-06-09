package com.alibaba.cola.job;

import com.alibaba.cola.job.model.ExecutionStatus;
import com.alibaba.cola.job.model.Job;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JobLauncher {
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /**
     * Execute job in sync mode
     *
     * @param job
     * @return
     */
    public static JobExecution executeSync(Job job) {
        return executeSync(job, new ExecutionContext());
    }

    /**
     * Execute job in sync mode
     *
     * @param job , the job to be executed
     * @param executionContext, the context to be used in execution runtime
     * @return
     */
    public static JobExecution executeSync(Job job, ExecutionContext executionContext) {
        // 1. 创建任务
        JobExecution jobExecution = createJobExecution(job, executionContext);

        // 2. 执行任务
        job.execute(jobExecution);
        return jobExecution;
    }

    /**
     * Execute job in Async mode
     *
     * @param job
     * @return jobExecutionId instantly
     */
    public static String executeAsync(Job job) {
        return executeAsync(job, new ExecutionContext(), null);
    }

    public static String executeAsync(Job job, ExecutorService executorService) {
        return executeAsync(job, new ExecutionContext(), executorService);
    }

    public static String executeAsync(Job job, ExecutionContext executionContext) {
        return executeAsync(job, executionContext, null);
    }

    public static String executeAsync(Job job, ExecutionContext executionContext, ExecutorService executorService) {
        // 1. 创建任务
        final JobExecution jobExecution = createJobExecution(job, executionContext);

        // 2. 执行任务
        if (executorService != null) {
            // 使用自定义的线程池执行任务
            executorService.submit(() -> {
                job.execute(jobExecution);
            });
        } else {
            // 使用默认的线程池执行任务
            DEFAULT_EXECUTOR_SERVICE.submit(() -> {
                job.execute(jobExecution);
            });
        }

        // 3. 立即返回任务执行id，任务异步执行
        return jobExecution.getJobId();
    }

    private static JobExecution createJobExecution(Job job, ExecutionContext executionContext) {
        JobRepository jobRepository = job.getJobRepository();
        JobExecution jobExecution = null;
        if (StringUtils.hasLength(executionContext.getJobId())) {
            jobExecution = jobRepository.getJobExecutionByJobId(executionContext.getJobId());
        }
        if (jobExecution == null) {
            // 全新的任务
            jobExecution = new JobExecution();
            jobExecution.setJobId(UuidGenerator.nextJobId());
            jobExecution.setJobDef(job);
            jobExecution.setExecutionStatus(ExecutionStatus.CREATED);
            jobExecution.setExecutionContext(executionContext);
            jobExecution.setBatchJobId(executionContext.getBatchJobId());
            log.info("[Job] created: {}", jobExecution);
            jobRepository.saveJobExecution(jobExecution);
        } else {
            // 已经存在的任务
            jobExecution.setJobDef(job);
            jobExecution.setStepExecutionList(jobRepository.listStepExecutions(jobExecution.getJobId()));
        }
        return jobExecution;
    }
}

