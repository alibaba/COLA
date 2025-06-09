package com.alibaba.cola.job.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.Objects;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = {
        "com.alibaba.cola.job.repository.db"
})
@EntityScan(basePackages = "com.alibaba.cola.job.model")
@ConditionalOnProperty(value = "cola.job.repository-type", havingValue = "DB")
public class DBAutoConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private JobProperties jobProperties;

    @PostConstruct
    public void init() {
        log.info("DBAutoConfiguration jobDatabaseProperties: {}", jobProperties.getDatabase());
        if(jobProperties == null || jobProperties.getDatabase() == null) {
            log.warn("DBAutoConfiguration jobProperties or jobProperties.getDatabase() is null, skip init sql");
            return;
        }

        if (jobProperties.getDatabase().getAutoDdl()) {
            log.info("DBAutoConfiguration start init sql");
            String schema = "schema-mysql.sql";
            // 可以通过ddl-location覆盖defaultSchema
            String customizedSchema = jobProperties.getDatabase().getDdlLocation();
            if (Objects.nonNull(customizedSchema) && !customizedSchema.isEmpty()) {
                schema = customizedSchema;
            }
            DatabasePopulator databasePopulator = new ResourceDatabasePopulator(
                    new ClassPathResource(schema));
            DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        }
    }
}

