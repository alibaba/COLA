package com.alibaba.cola.mock.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.exceptions.ErrorContext;
import com.alibaba.cola.mock.model.MockData;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.utils.CommonUtils;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.SpyHelper;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/13
 */
public class UnitTestListener extends RunListener{
    private static final Logger logger = LoggerFactory.getLogger(UnitTestListener.class);
    SpyHelper spyHelper;

    @Override
    public void testStarted(Description description) throws Exception {
        spyHelper = new SpyHelper(description.getTestClass(), ColaMockito.g().getContext().getTestInstance());
        //开始前先清理repo
        ColaMockito.g().getFileDataEngine().clean();
        readTestMethodConfig(description);
    }

    @Override
    public void testRunStarted(Description description){
        ColaMockito.g().getContext().setTestMeta(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception{
    }

    @Override
    public void testFinished(Description description) throws Exception {
        spyHelper.resetTest();
        if(ErrorContext.isError()){
            ErrorContext.reset();
            return;
        }

        boolean remain = ColaMockito.g().getFileDataEngine().validHasRemainData();
        if(remain){
            throw new RuntimeException("remain data,please clean! testMethod=>" + description.getDisplayName());
        }
        printPrettyColaConfig();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        ErrorContext.instance().cause(failure.getException()).message(failure.getMessage());
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

    //private void resetMock(Class testClazz){
    //    //List<MockServiceModel> mockServiceModelList = ColaMockito.g().getContext().getSpyList();
    //    //if(mockServiceModelList == null || mockServiceModelList.size() == 0){
    //    //    return;
    //    //}
    //    //
    //    //for(MockServiceModel model : mockServiceModelList){
    //    //    Mockito.reset(model.getMock());
    //    //}
    //    spyHelper.resetTest();
    //}

    private void printPrettyColaConfig(){
        ColaTestModel colaTestModel = ColaMockito.g().getCurrentTestModel();
        if(colaTestModel == null){
            return;
        }
        Class[] mocks = colaTestModel.getColaMockConfig().mocks();
        Set<String> clazzNameSet = getAllActualMockClasses();
        if(mocks.length != clazzNameSet.size()){
            logger.info("PrettyColaConfig => " + String.format(Constants.COLAMOCKCONFIG_TEMPLATE, CommonUtils.format2ColaConfigClazz(clazzNameSet)));
        }
    }

    private Set<String> getAllActualMockClasses(){
        Set<String> clazzNameSet = new HashSet<>();
        if(!ColaMockito.g().getFileDataEngine().isExsitsMockDataFileByFileId(ColaMockito.g().getCurrentTestUid())){
            return clazzNameSet;
        }
        MockDataFile mockDataFile = ColaMockito.g().getFileDataEngine().getMockDataFileByFileId(ColaMockito.g().getCurrentTestUid());

        List<MockData> mockDataLst = mockDataFile.getAllMockData();
        for(MockData mockData : mockDataLst){
            clazzNameSet.add(mockData.getDataId().split(Constants.UNDERLINE)[0]);
        }
        return clazzNameSet;
    }
}
