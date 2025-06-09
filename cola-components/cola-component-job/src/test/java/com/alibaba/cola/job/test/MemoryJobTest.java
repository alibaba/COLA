package com.alibaba.cola.job.test;


import com.alibaba.cola.job.repository.JobRepository;
import com.alibaba.cola.job.repository.memory.MemoryJobRepository;
import org.junit.jupiter.api.Test;

public class MemoryJobTest extends AbstractBaseJobTest {

    private static JobRepository jobRepository = new MemoryJobRepository();

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
    public void testJobRollbackFailed() {
        super.jobRollBackFailed();
    }
}

