package com.alibaba.sofa.test;

import com.alibaba.sofa.TestConfig;
import com.alibaba.sofa.boot.Bootstrap;
import com.alibaba.sofa.boot.RegisterFactory;
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
