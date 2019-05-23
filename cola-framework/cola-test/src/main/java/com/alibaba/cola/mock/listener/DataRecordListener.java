package com.alibaba.cola.mock.listener;

import com.alibaba.cola.container.command.AbstractCommand;
import com.alibaba.cola.container.command.TestMethodRunCmd;
import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.utils.SpyHelper;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.cola.mock.ColaMockito.g;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/13
 */
public class DataRecordListener extends RunListener{
    private static final Logger logger = LoggerFactory.getLogger(DataRecordListener.class);
    SpyHelper spyHelper;

    @Override
    public void testStarted(Description description) throws Exception {
        ColaMockito.g().getContext().getColaTestMeta().setDescription(description);
        //开始前先清理repo
        ColaMockito.g().getFileDataEngine().clean();
        spyHelper = new SpyHelper(description.getTestClass(), ColaMockito.g().getContext().getTestInstance());
        spyHelper.processInject4Record();
        if(isSegmentRecord()){
            preLoadFileData();
            readTestMethodConfig(description);
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        //重置mockito行为
        spyHelper.resetRecord();

        //输出调用栈
        logger.info(ColaMockito.g().getContext().getStackTree().toString());
        ColaMockito.g().getContext().clean();

        //持久化到磁盘
        ColaMockito.g().getFileDataEngine().flush();
    }

    @Override
    public void testRunStarted(Description description){
        reScanTestClass(description.getTestClass());
    }

    private void reScanTestClass(Class testClz){
        ColaTestModel colaTestModel = ColaMockito.g().scanColaTest(testClz);
        ColaMockito.g().getContext().putColaTestModel(colaTestModel);
    }

    private boolean isSegmentRecord(){
        AbstractCommand command = ColaMockito.g().getContext().getColaTestMeta().getCommand();
        if(!(command instanceof TestMethodRunCmd)){
            return false;
        }
        TestMethodRunCmd testMethodRunCmd = (TestMethodRunCmd)command;
        return testMethodRunCmd.isSegmentRecord();
    }

    private void preLoadFileData(){
        MockDataFile mockDataFile = ColaMockito.g().getFileDataEngine().getMockDataFileByFileId(g().getCurrentTestUid());
        InputParamsFile inputParamsFile = ColaMockito.g().getFileDataEngine().getInputParamsFileByFileId(g().getCurrentTestUid() + "_inputParams");

        mockDataFile.getHeader().setInitialized(false);
        inputParamsFile.getHeader().setInitialized(false);
    }

    /**
     * 初始化当前运行方法配置
     * @param description
     */
    private void readTestMethodConfig(Description description) {

        ExcludeCompare noNeedCompareConfig = description.getAnnotation(ExcludeCompare.class);

        ColaTestModel currentTestClassConfig = ColaMockito.g().getContext().getColaTestModelMap().get(description.getTestClass());
        if(currentTestClassConfig != null){
            currentTestClassConfig.putCurrentTestMethodConfig(ColaMockito.g().getCurrentTestUid(), noNeedCompareConfig);
        }
    }
}
