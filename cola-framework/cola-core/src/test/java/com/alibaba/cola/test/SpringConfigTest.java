package com.alibaba.cola.test;

import com.alibaba.cola.TestConfig;
import com.alibaba.cola.boot.Bootstrap;
import com.alibaba.cola.boot.RegisterFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringConfigTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        Bootstrap bootstrap = (Bootstrap)context.getBean("bootstrap");
        RegisterFactory registerFactory = (RegisterFactory)context.getBean("registerFactory");
        System.out.println(registerFactory);
        System.out.println(bootstrap.getPackages());
    }
}
