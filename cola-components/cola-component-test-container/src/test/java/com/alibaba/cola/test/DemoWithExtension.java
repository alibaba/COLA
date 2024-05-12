package com.alibaba.cola.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = SpringBootConfig.class)
@ExtendWith(LoggingExtension.class)
public class DemoWithExtension {

    @Autowired
    private Demo demo;

    @BeforeEach
    public void before() {
        System.out.println("=====before");
    }

    @Test
    public void testParam(String param) {
        System.out.println("hello : " + param);
    }

    @Test
    public void testMethod1() {
        System.out.println("Begin testMethod1");
        demo.testOne();
        System.out.println("End testMethod1");
    }

    @Test
    public void testMethod2() {
        System.out.println("Begin testMethod2");
        demo.testTwo();
        System.out.println("End testMethod2");
    }

    @AfterEach
    public void after() {
        System.out.println("=====after");
    }
}


class LoggingExtension implements BeforeEachCallback, ParameterResolver {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        System.out.println("Executing test method: " + context.getRequiredTestMethod().getName());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        System.out.println("resolveParameter: " + parameterContext);
        return null;
    }
}
