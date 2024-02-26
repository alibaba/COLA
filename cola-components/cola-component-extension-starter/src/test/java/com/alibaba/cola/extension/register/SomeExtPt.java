package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.ExtensionPointI;

public interface SomeExtPt extends ExtensionPointI {

    void doSomeThing();

    void doSomeThingWithException() throws Exception;

}
