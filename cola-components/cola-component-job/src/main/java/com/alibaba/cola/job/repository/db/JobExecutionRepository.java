package com.alibaba.cola.job.repository.db;


import com.alibaba.cola.job.model.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, String> {
    JobExecution getByJobId(String jobId);
}
