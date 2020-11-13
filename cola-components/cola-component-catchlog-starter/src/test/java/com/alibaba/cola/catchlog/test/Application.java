package com.alibaba.cola.catchlog.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @author Frank Zhang
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = {"com.alibaba.cola.catchlog"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
