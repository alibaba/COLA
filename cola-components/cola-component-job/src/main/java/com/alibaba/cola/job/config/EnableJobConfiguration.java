package com.alibaba.cola.job.config;

import com.alibaba.cola.job.JobException;
import com.alibaba.cola.job.repository.JobRepository;
import com.alibaba.cola.job.repository.JsonUtil;
import com.alibaba.cola.job.repository.RepositoryType;
import com.alibaba.cola.job.repository.db.DataBaseJobRepository;
import com.alibaba.cola.job.repository.memory.MemoryJobRepository;
import com.alibaba.cola.job.repository.redis.RedisJobRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Slf4j
@Import( {JobProperties.class, DBAutoConfiguration.class, RedisConfig.class, JsonUtil.class})
@ComponentScan("com.alibaba.cola.job")
public class EnableJobConfiguration {

    @Resource
    private JobProperties jobProperties;

    @Bean
    public JobRepository jobRepository() {
        if (jobProperties.getRepositoryType() == null) {
            jobProperties.setRepositoryType(RepositoryType.MEMORY);
        }
        JobRepository jobRepository = switch (jobProperties.getRepositoryType()) {
            case DB -> new DataBaseJobRepository();
            case REDIS -> new RedisJobRepository();
            case MEMORY -> new MemoryJobRepository();
            default -> throw new JobException("not support repositoryType: " + jobProperties.getRepositoryType());
        };
        log.info("[cola-job]create JobRepository: " + jobRepository.getClass().getSimpleName());
        return jobRepository;
    }
}

