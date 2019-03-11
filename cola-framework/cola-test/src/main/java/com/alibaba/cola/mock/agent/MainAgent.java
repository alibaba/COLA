package com.alibaba.cola.mock.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class MainAgent {
    public static void agentmain(String agentArgs, Instrumentation inst)
        throws ClassNotFoundException, UnmodifiableClassException,
        InterruptedException {
        System.out.println("Agent Main Done");
        final ColaTransformer transformer = new ColaTransformer(agentArgs);
        inst.addTransformer(transformer, true);
    }
}
