package com.alibaba.cola;

import com.alibaba.cola.boot.Bootstrap;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TestConfig
 *
 * @author Frank Zhang 2018-01-06 7:57 AM
 */
@Configuration
@ComponentScan("com.alibaba.cola")
@PropertySource(value = {"/sample.properties"})
public class TestConfig {

    @Bean(initMethod = "init")
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        List<String> packagesToScan  = new ArrayList<>();
        packagesToScan.add("com.alibaba.cola.test");
        bootstrap.setPackages(packagesToScan);
        return bootstrap;
    }
}
