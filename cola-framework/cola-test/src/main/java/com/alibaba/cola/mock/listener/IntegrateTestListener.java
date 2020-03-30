package com.alibaba.cola.mock.listener;

import java.util.HashSet;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.utils.SpyHelper;

import org.junit.runner.Description;
import org.junit.runner.Result;

/**
 * @author shawnzhan.zxy
 * @date 2019/02/19
 */
public class IntegrateTestListener extends UnitTestListener{
    SpyHelper spyHelper;

    @Override
    public void testStarted(Description description) throws Exception {
        spyHelper = new SpyHelper(description.getTestClass(), ColaMockito.g().getContext().getTestInstance());
        spyHelper.processInject4Test(new HashSet<>());
        super.testStarted(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        spyHelper.resetTest();
        super.testFinished(description);
    }
}
