package com.alibaba.cola.mock.listener;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.utils.SpyHelper;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/13
 */
public class DataRecordListener extends RunListener{
    SpyHelper spyHelper;

    @Override
    public void testStarted(Description description) throws Exception {
        //开始前先清理repo
        ColaMockito.g().getFileDataEngine().clean();
        spyHelper = new SpyHelper(description.getTestClass(), ColaMockito.g().getContext().getTestInstance());
        spyHelper.processInject4Record();
    }

    @Override
    public void testRunStarted(Description description){
        reScanTestClass(description.getTestClass());
        ColaMockito.g().getContext().setTestMeta(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        spyHelper.resetRecord();
        //记录模式才持久化存储
        ColaMockito.g().getFileDataEngine().flush();
        ColaMockito.g().getFileDataEngine().flushInputParamsFile();
    }

    private void reScanTestClass(Class testClz){
        ColaTestModel colaTestModel = ColaMockito.g().scanColaTest(testClz);
        ColaMockito.g().getContext().putColaTestModel(colaTestModel);
    }
}
