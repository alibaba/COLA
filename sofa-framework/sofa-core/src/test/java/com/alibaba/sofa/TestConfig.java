package com.alibaba.sofa;

import com.alibaba.sofa.boot.Bootstrap;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TestConfig
 *
 * @author Frank Zhang 2018-01-06 7:57 AM
 */
@Configuration
@ComponentScan("com.alibaba.sofa")
@PropertySource(value = {"/sample.properties"})
public class TestConfig {

    @Bean(initMethod = "init")
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        List<String> packagesToScan  = new ArrayList<>();
        packagesToScan.add("com.alibaba.sofa.test");
        bootstrap.setPackages(packagesToScan);
        return bootstrap;
    }
}
