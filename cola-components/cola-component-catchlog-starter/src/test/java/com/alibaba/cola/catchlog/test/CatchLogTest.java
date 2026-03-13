package com.alibaba.cola.catchlog.test;

import com.alibaba.cola.catchlog.CatchLogAspect;
import com.alibaba.cola.catchlog.CatchLogAutoConfiguration;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 */
@SpringBootTest(classes = {CatchLogAutoConfiguration.class, Demo.class, CatchLogAspect.class, Application.class})
public class CatchLogTest {

    @Resource
    private Demo demo;

    @Test
    public void testAspect() {
        demo.doSomething();
    }

    @Test
    public void testCatchAndLog() {
        Demo.Request request = new Demo.Request();
        request.name = "Frank";
        request.age = 18;
        demo.execute(request);
    }

    @Test
    public void testExecuteWithResponse(){
        Demo.Request request = new Demo.Request();
        request.name = "Frank";
        request.age = 18;
        demo.executeWithResponse(request);
    }

    @Test
    public void testExecuteWithVoid(){
        demo.executeWithVoid();
    }

    @Test
    public void testExecuteWithExceptionAndVoid(){
        demo.executeWithExceptionAndVoid();
    }

    @Test
    public void testExecuteWithExceptionAndDemoResponse(){
        demo.executeWithExceptionAndDemoResponse();
    }

    @Test
    public void testExecuteWithBizExceptionAndResponse(){
        demo.executeWithBizExceptionAndResponse();
    }

    @Test
    public void testExecuteWithSysExceptionAndResponse(){
        demo.executeWithSysExceptionAndResponse();
    }

    @Test
    public void testExecuteWithExceptionAndResponse(){
        demo.executeWithExceptionAndResponse();
    }

}
