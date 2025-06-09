package com.alibaba.cola.job.model;

import com.alibaba.cola.job.JobException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public enum ExecutionStatus {
    CREATED,
    STARTED,
    COMPLETED,
    FAILED,
    RUNNING,
    ROLLBACK_STARTED,
    ROLLBACK_COMPLETED,
    ROLLBACK_FAILED,
    UNKNOWN;

    public final static List<ExecutionStatus> ROLLBACK_STATUS = List.of(ROLLBACK_STARTED, ROLLBACK_FAILED,
            ROLLBACK_COMPLETED);

    @JsonCreator
    public static ExecutionStatus fromValue(String value) {
        for (ExecutionStatus s : ExecutionStatus.values()) {
            if (s.name().equals(value)) {
                return s;
            }
        }
        throw new JobException("Unexpected value '" + value + "'");
    }

    public static boolean isRollback(ExecutionStatus executionStatus) {
        return executionStatus != null && ROLLBACK_STATUS.contains(executionStatus);
    }

    public static boolean isTerminated(ExecutionStatus executionStatus) {
        return executionStatus == ExecutionStatus.COMPLETED
                || executionStatus == ExecutionStatus.FAILED
                || isRollback(executionStatus);
    }
}

