package com.alibaba.craftsman;

import com.alibaba.craftsman.config.CraftsmanConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Starter
 *
 * COLA framework initialization is configured in {@link CraftsmanConfig}
 *
 * @author Frank Zhang
 */
@SpringBootApplication(scanBasePackages = {"com.alibaba.craftsman","com.alibaba.cola"})
@MapperScan("com.alibaba.craftsman.gatewayimpl.database")
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("Begin to start Spring Boot Application");
        long startTime = System.currentTimeMillis();

        SpringApplication.run(Application.class, args);

        long endTime = System.currentTimeMillis();
        log.info("End starting Spring Boot Application, Time used: "+ (endTime - startTime) );
    }
}
