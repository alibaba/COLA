package com.alibaba.cola.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Demo {

    @Before
    public void before(){
        System.out.println("before action");
    }

    @Test
    public void testOne(){
        System.out.println("test one");
    }

    @Test
    public void testTwo(){
        System.out.println("test two");
    }

    @After
    public void after(){
        System.out.println("after action");
    }
}
