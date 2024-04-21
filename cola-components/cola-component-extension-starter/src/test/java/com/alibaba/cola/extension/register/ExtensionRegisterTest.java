package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionException;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.extension.Application;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class ExtensionRegisterTest {

    @Resource
    private ExtensionRegister register;

    @Resource
    private ExtensionExecutor executor;

    @Test
    public void testDuplicateRegistration() {
        // expect:
        //Duplicate registration is not allowed for :ExtensionCoordinate
        // [extensionPointName=com.alibaba.cola.extension.register.SomeExtPt, bizScenarioUniqueIdentity=A.#defaultUseCase#.#defaultScenario#]
        Assertions.assertThrows(ExtensionException.class, ()->{
            SomeExtPt extA = new SomeExtensionA();
            register.doRegistration(extA);

            executor.executeVoid(SomeExtPt.class, BizScenario.valueOf("A"), SomeExtPt::doSomeThing);
        });
    }

}
