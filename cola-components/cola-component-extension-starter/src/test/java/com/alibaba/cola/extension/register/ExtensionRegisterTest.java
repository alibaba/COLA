package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.extension.test.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ExtensionRegisterTest {

    @Resource
    private ExtensionRegister register;

    @Resource
    private ExtensionExecutor executor;

    private static boolean init =false;

    @Before
    public void init() {
        if (!init) {
            SomeExtPt extA = new SomeExtensionA();
            register.doRegistration(extA);

            SomeExtPt extB = CglibProxyFactory.createProxy(new SomeExtensionB());
            register.doRegistration(extB);
            init = true;
        }
    }

    @Test
    public void test() {
        executor.executeVoid(SomeExtPt.class, BizScenario.valueOf("A"), SomeExtPt::doSomeThing);
        executor.executeVoid(SomeExtPt.class, BizScenario.valueOf("B"), SomeExtPt::doSomeThing);
    }

    @Test(expected = RuntimeException.class)
    public void testRuntimeException() {
        executor.executeVoidWithException(SomeExtPt.class, BizScenario.valueOf("A"), SomeExtPt::doSomeThingWithException);
    }

    @Test(expected = Exception.class)
    public void testException() {
        executor.executeVoidWithException(SomeExtPt.class, BizScenario.valueOf("B"), SomeExtPt::doSomeThingWithException);
    }

}
