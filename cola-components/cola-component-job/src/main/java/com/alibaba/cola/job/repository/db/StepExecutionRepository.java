package com.alibaba.cola.job.repository.db;

import com.alibaba.cola.job.model.StepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepExecutionRepository extends JpaRepository<StepExecution, String> {
    StepExecution getByStepId(String stepId);
    StepExecution getByJobIdAndStepName(String jobId, String stepName);
    List<StepExecution> findByJobId(String jobId);
}

