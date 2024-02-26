package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.Extension;

import org.springframework.stereotype.Component;

@Extension(bizId = "B")
@Component
public class SomeExtensionB implements SomeExtPt {

    @Override
    public void doSomeThing() {
        System.out.println("SomeExtensionB::doSomething");
    }

    @Override
    public void doSomeThingWithException() throws Exception {
        System.out.println("SomeExtensionA::throw Exception");
        throw new Exception();
    }

}
