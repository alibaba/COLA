package com.alibaba.cola;

import com.alibaba.cola.boot.SpringBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * TestSpringConfig
 *
 * @author Frank Zhang
 * @date 2020-06-18 8:03 PM
 */
@Configuration
@ComponentScan(value = {"com.alibaba.cola","com.alibaba.cola.test"})
@PropertySource(value = {"/sample.properties"})
public class TestSpringConfig {

    @Bean(initMethod = "init")
    public SpringBootstrap bootstrap() {
        SpringBootstrap bootstrap = new SpringBootstrap();
        return bootstrap;
    }

}
