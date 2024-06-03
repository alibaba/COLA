package com.alibaba.cola.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SpringConfig
 *
 * @author Frank Zhang
 * @date 2020-11-17 5:11 PM
 */
@Configuration
@ComponentScan
public class SpringConfig {

    @Bean("demo")
    public Demo generateDemo(){
        return new Demo();
    }
}
