package com.alibaba.cola.job.repository.db;


import com.alibaba.cola.job.model.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, String> {
    BatchJobExecution getByBatchJobId(String batchJobId);

    @Query("SELECT b FROM BatchJobExecution b " +
            "WHERE b.status <> 'COMPLETED' " +
            "AND b.startTime < :thresholdTime")
    List<BatchJobExecution> findNotCompletedBatchJobsOlderThan(
            @Param("thresholdTime") LocalDateTime thresholdTime);
}

