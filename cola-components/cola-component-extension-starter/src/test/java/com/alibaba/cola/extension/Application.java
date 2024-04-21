package com.alibaba.cola.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application
 *
 * @author Frank Zhang
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.alibaba.cola")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
