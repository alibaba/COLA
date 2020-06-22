package com.alibaba.cola.test;

import com.alibaba.cola.TestSpringConfig;
import com.alibaba.cola.boot.SpringBootstrap;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * SpringConfigTest
 *
 * @author Frank Zhang
 * @date 2020-06-18 8:09 PM
 */
public class SpringConfigTest {
    public static void main(String[] args) {
        LoggerFactory.activateSysLogger();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);
        SpringBootstrap bootstrap = (SpringBootstrap)context.getBean("bootstrap");
        System.out.println(bootstrap);
    }
}
