package com.alibaba.cola.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = SpringBootConfig.class)
@ExtendWith(LoggingExtension.class)
public class DemoWithExtension{

    @Resource
    private Demo demo;

    @BeforeEach
    public void before(){
        System.out.println("=====before");
    }

    @Test
    public void testMethod1() {
        System.out.println("Begin testMethod1");
        demo.testOne();
        System.out.println("End testMethod1");
    }

    @Test
    public void testMethod2(){
        System.out.println("Begin testMethod2");
        demo.testTwo();
        System.out.println("End testMethod2");
    }

    @AfterEach
    public void after(){
        System.out.println("=====after");
    }
}


class LoggingExtension implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("Before Executing test method: " + context.getRequiredTestMethod().getName());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        System.out.println("After Executed test method: " + context.getRequiredTestMethod().getName());
    }
}
