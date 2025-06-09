package com.alibaba.cola.job.test;

import com.alibaba.cola.job.config.EnableColaJob;
import com.alibaba.cola.job.repository.JobRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("h2-test")
public class MemoryDBJobTest extends AbstractBaseJobTest {

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
    public void test() {
        super.jobRollBackFailed();
    }
}

