package com.alibaba.cola.job;

import com.alibaba.cola.job.model.Job;
import com.alibaba.cola.job.model.Step;
import com.alibaba.cola.job.repository.JobRepository;

public class JobBuilderFactory {
    public static JobBuilder create() {
        return new JobBuilder();
    }

    public static class JobBuilder {
        private final Job job = new Job();

        public JobBuilder addStep(Step step) {
            step.setJob(job);
            job.getSteps().add(step);
            return this;
        }

        public JobBuilder name(String name) {
            job.setName(name);
            return this;
        }

        public JobBuilder needRollback(boolean needRollback) {
            job.setNeedRollback(needRollback);
            return this;
        }

        public JobBuilder isAsync(boolean isAsync) {
            job.setAsync(isAsync);
            return this;
        }

        public JobBuilder jobRepository(JobRepository jobRepository) {
            job.setJobRepository(jobRepository);
            return this;
        }

        public Job build(String name, JobRepository jobRepository) {
            job.setName(name);
            job.setJobRepository(jobRepository);
            return job;
        }

        public Job build() {
            return job;
        }
    }
}

