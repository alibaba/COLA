package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.Extension;

import org.springframework.stereotype.Component;

@Extension(bizId = "A")
@Component
public class SomeExtensionA implements SomeExtPt {

    @Override
    public void doSomeThing() {
        System.out.println("SomeExtensionA::doSomething");
    }

    @Override
    public void doSomeThingWithException() throws Exception {
        System.out.println("SomeExtensionA::throw RuntimeException");
        throw new RuntimeException();
    }

}
