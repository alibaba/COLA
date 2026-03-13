package com.alibaba.cola.job.test;

import com.alibaba.cola.job.BatchJobLauncher;
import com.alibaba.cola.job.ExecutionContext;
import com.alibaba.cola.job.JobBuilderFactory;
import com.alibaba.cola.job.JobLauncher;
import com.alibaba.cola.job.model.*;
import com.alibaba.cola.job.repository.JobRepository;
import com.alibaba.cola.job.test.steps.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;

@Slf4j
public abstract class AbstractBaseJobTest {

    public abstract JobRepository getJobRepository();

    public abstract void testBatchJobCompleted();

    public void batchJobCompleted() {
        Job job = JobBuilderFactory.create()
                .addStep(new MyStep1())
                .addStep(new MyStep2())
                .addStep(new LongTimeStep())
                .build("batchJob", getJobRepository());

        ExecutionContext context1 = new ExecutionContext();
        context1.putString("hostId", "111");
        JobInstance jobInstance1 = new JobInstance(job, context1);

        ExecutionContext context2 = new ExecutionContext();
        context2.putString("hostId", "222");
        JobInstance jobInstance2 = new JobInstance(job, context2);

        ExecutionContext context3 = new ExecutionContext();
        context3.putString("hostId", "333");
        JobInstance jobInstance3 = new JobInstance(job, context3);

        BatchJob batchJob = new BatchJob();
        batchJob.add(jobInstance1)
                .add(jobInstance2)
                .add(jobInstance3)
                .jobRepository(getJobRepository())
                .executorService(buildExecutorService());

        BatchJobExecution batchJobExecution = BatchJobLauncher.execute(batchJob);

        // 最长等待5秒，每1秒钟检查一下，看batchJob状态是不是COMPLETED。超过5秒，报错
        await().atMost(7, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
            boolean isCompleted = BatchJobLauncher.checkAndRefresh(batchJob, batchJobExecution.getBatchJobId());
            return isCompleted;
        });
    }

    public ExecutorService buildExecutorService() {
        // 创建自定义的 ThreadFactory
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("CustomThread-" + count.getAndIncrement());
                return thread;
            }
        };

        // 使用自定义的 ThreadFactory 创建 ExecutorService
        ExecutorService executorService = new ThreadPoolExecutor(2, // corePoolSize
                5, // maximumPoolSize
                60L, // keepAliveTime
                TimeUnit.SECONDS, // timeUnit
                new LinkedBlockingQueue<>(), // workQueue
                namedThreadFactory // 使用自定义的 ThreadFactory
        );
        return executorService;
    }

    public abstract void testBatchJobFailed();

    public void batchJobFailed() {
        Job job = JobBuilderFactory.create()
                .addStep(new MyStep1())
                .addStep(new MyStep2())
                .addStep(new FailedStep())
                .build("batchJob", getJobRepository());

        ExecutionContext context1 = new ExecutionContext();
        context1.putString("hostId", "111");
        JobInstance jobInstance1 = new JobInstance(job, context1);

        ExecutionContext context2 = new ExecutionContext();
        context2.putString("hostId", "222");
        JobInstance jobInstance2 = new JobInstance(job, context2);

        ExecutionContext context3 = new ExecutionContext();
        context3.putString("hostId", "333");
        JobInstance jobInstance3 = new JobInstance(job, context3);

        BatchJob batchJob = new BatchJob();
        batchJob.add(jobInstance1).add(jobInstance2).add(jobInstance3).jobRepository(getJobRepository());

        BatchJobExecution batchJobExecution = BatchJobLauncher.execute(batchJob);

        // 最长等待5秒，每300ms钟检查一下，看batchJob状态是不是Failed
        await().atMost(5, TimeUnit.SECONDS).pollInterval(300, TimeUnit.MILLISECONDS).until(() -> {
            BatchJobLauncher.checkAndRefresh(batchJob, batchJobExecution.getBatchJobId());
            BatchJobExecution result = getJobRepository().getBatchJobExecutionByBatchJobId(
                    batchJobExecution.getBatchJobId());
            return result.isFailed();
        });
    }

    public abstract void testJobCompleted();

    public void jobCompleted() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.COMPLETED, jobExecution.getExecutionStatus());
    }

    public void jobRunning() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        jobBuilder.isAsync(true);
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.RUNNING, jobExecution.getExecutionStatus());
    }

    public abstract void testJobWithCustomizedParam();

    public void jobWithCustomizedParam() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep2());
        Job job = jobBuilder.build("myJob", getJobRepository());
//        ExecutionContext<JobRequest> executionContext = new ExecutionContext<>();
//        executionContext.setParam(JobRequest.mock());
        ExecutionContext executionContext = new ExecutionContext();
        JobExecution jobExecution = JobLauncher.executeSync(job, executionContext);

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.COMPLETED, jobExecution.getExecutionStatus());
    }

    public abstract void testJobRerunSkipNext();

    public void jobRerunSkipNext() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        ExecutionContext executionContext = new ExecutionContext(jobExecution.getJobId());
        JobLauncher.executeSync(job, executionContext);

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.COMPLETED, jobExecution.getExecutionStatus());
    }

    public abstract void testJobRerunFixPreviousFail();

    public void jobRerunFixPreviousFail() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new SwitchStep());
        Job job = jobBuilder.build("myJob", getJobRepository());

        JobExecution jobExecution = JobLauncher.executeSync(job);
        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.FAILED, jobExecution.getExecutionStatus());

        ExecutionContext executionContext = new ExecutionContext(jobExecution.getJobId());
        JobLauncher.executeSync(job, executionContext);
        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.COMPLETED, jobExecution.getExecutionStatus());
    }

    public abstract void testStepRollBack();

    public void stepRollBack() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        jobBuilder.addStep(new MyStep3());
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        StepExecution stepExecution = getJobRepository().getStepExecution(jobExecution.getJobId(),
                MyStep3.class.getSimpleName());
        Assertions.assertEquals(ExecutionStatus.ROLLBACK_COMPLETED, stepExecution.getExecutionStatus());

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.FAILED, jobExecution.getExecutionStatus());
    }

    public abstract void testStepRollbackFailed();

    public void stepRollbackFailed() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        jobBuilder.addStep(new MyStep4());
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        StepExecution stepExecution = getJobRepository().getStepExecution(jobExecution.getJobId(),
                MyStep4.class.getSimpleName());
        Assertions.assertEquals(ExecutionStatus.ROLLBACK_FAILED, stepExecution.getExecutionStatus());

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.FAILED, jobExecution.getExecutionStatus());
    }

    public void jobRollBack() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new MyStep2());
        jobBuilder.addStep(new MyStep3());
        Job job = jobBuilder.needRollback(true).build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        for (Step step : job.getSteps()) {
            StepExecution stepExecution = getJobRepository().getStepExecution(jobExecution.getJobId(),
                    step.getClass().getSimpleName());
            Assertions.assertEquals(ExecutionStatus.ROLLBACK_COMPLETED, stepExecution.getExecutionStatus());
        }
        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.ROLLBACK_COMPLETED, jobExecution.getExecutionStatus());
    }

    public void jobRollBackFailed() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();

        MyStep5 rollbackErrorStep = new MyStep5();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(rollbackErrorStep);
        jobBuilder.addStep(new MyStep3());
        Job job = jobBuilder.needRollback(true).build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        expectedStatus(jobExecution.getJobId(),
                Map.of("MyStep1", ExecutionStatus.COMPLETED, "MyStep5", ExecutionStatus.ROLLBACK_FAILED, "MyStep3",
                        ExecutionStatus.ROLLBACK_COMPLETED));

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.ROLLBACK_FAILED, jobExecution.getExecutionStatus());

        // now step status = COMPLETED, ROLLBACK_FAILED ,ROLLBACK_COMPLETED
        // continue rollback will skip third, rollback second and first
        log.info("start continue rollback");
        rollbackErrorStep.setRollbackThrow(false);

        jobExecution.getExecutionContext().setJobId(jobExecution.getJobId());
        jobExecution = JobLauncher.executeSync(job, jobExecution.getExecutionContext());

        // 这里需要补充全部的状态检查
        expectedStatus(jobExecution.getJobId(),
                Map.of("MyStep1", ExecutionStatus.ROLLBACK_COMPLETED, "MyStep5", ExecutionStatus.ROLLBACK_COMPLETED,
                        "MyStep3", ExecutionStatus.ROLLBACK_COMPLETED));
    }

    public void expectedStatus(String executionId, Map<String, ExecutionStatus> ExecutionStatus) {
        for (var entry : ExecutionStatus.entrySet()) {
            StepExecution stepExecution = getJobRepository().getStepExecution(executionId, entry.getKey());
            Assertions.assertEquals(entry.getValue(), stepExecution.getExecutionStatus());
        }
    }
}

