package com.alibaba.cola.job.test;


import com.alibaba.cola.job.JobBuilderFactory;
import com.alibaba.cola.job.JobLauncher;
import com.alibaba.cola.job.model.ExecutionStatus;
import com.alibaba.cola.job.model.Job;
import com.alibaba.cola.job.model.JobExecution;
import com.alibaba.cola.job.repository.JobRepository;
import com.alibaba.cola.job.test.steps.FailedStep;
import com.alibaba.cola.job.test.steps.MyStep1;
import com.alibaba.cola.unittest.redis.RedisExtension;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@ExtendWith(RedisExtension.class)
@ActiveProfiles("redis-test")
public class RedisJobTest extends AbstractBaseJobTest {

    @Resource
    JobRepository jobRepository;

    @Override
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    @Test
    public void testBatchJobCompleted() {
        super.batchJobCompleted();
    }

    @Test
    public void testBatchJobFailed() {
        super.batchJobFailed();
    }

    @Test
    public void testJobCompleted() {
        super.jobCompleted();
    }

    @Test
    public void testJobRunning(){
        super.jobRunning();
    }

    @Test
    public void testJobFailed() {
        JobBuilderFactory.JobBuilder jobBuilder = JobBuilderFactory.create();
        jobBuilder.addStep(new MyStep1());
        jobBuilder.addStep(new FailedStep());
        Job job = jobBuilder.build("myJob", getJobRepository());
        JobExecution jobExecution = JobLauncher.executeSync(job);

        jobExecution = getJobRepository().getJobExecutionByJobId(jobExecution.getJobId());
        Assertions.assertEquals(ExecutionStatus.FAILED, jobExecution.getExecutionStatus());
    }

    @Test
    public void testJobWithCustomizedParam() {
        super.jobWithCustomizedParam();
    }

    @Test
    public void testJobRerunSkipNext() {
        super.jobRerunSkipNext();
    }

    @Test
    public void testJobRerunFixPreviousFail() {
        super.jobRerunFixPreviousFail();
    }

    @Test
    public void testStepRollBack() {
        super.stepRollBack();
    }

    @Test
    public void testStepRollbackFailed() {
        super.stepRollbackFailed();
    }

    @Test
    public void testJobRollback() {
        super.jobRollBack();
    }

    @Test
    public void testJobRollBackFailed() {
        super.jobRollBackFailed();
    }
}

