package com.alibaba.cola.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * TestsContainerTest
 *
 * @author Frank Zhang
 * @date 2020-11-17 4:55 PM
 */
public class TestsContainerTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);

        TestsContainer.start();
    }
}
