package com.alibaba.cola.job.config;

import com.alibaba.cola.job.repository.RepositoryType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cola.job")
public class JobProperties {
    private RepositoryType repositoryType;
    private DatabaseProperties database;

    // database配置，数据库配置会自动映射到 cola.job.database 路径下
    @Data
    public static class DatabaseProperties {
        private Boolean autoDdl;
        private String ddlLocation;
    }
}
