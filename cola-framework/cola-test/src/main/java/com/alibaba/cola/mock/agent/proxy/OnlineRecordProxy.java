package com.alibaba.cola.mock.agent.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.ColaRecordController;
import com.alibaba.cola.mock.ColaTestRecordController;
import com.alibaba.cola.mock.autotest.ColaTestGenerator;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.persist.DataStoreEnum;
import com.alibaba.cola.mock.persist.FileDataEngine;
import com.alibaba.cola.mock.utils.FileUtils;

import static com.alibaba.cola.mock.utils.Constants.INPUT_PARAMS_FILE_SUFFIX;
import static com.alibaba.cola.mock.utils.Constants.NOTE_SYMBOL;
import static com.alibaba.cola.mock.utils.Constants.UNDERLINE;

/**
 * @author shawnzhan.zxy
 * @date 2019/05/13
 */
public class OnlineRecordProxy{
    private static ThreadLocal<OnlineRecordProxy> proxyThreadLocal = new ThreadLocal<>();

    private Class mapperInterface;
    private Object[] paramterValues;
    public OnlineRecordProxy(Class mapperInterface){
        this.mapperInterface = mapperInterface;
    }

    public static OnlineRecordProxy get(Class mapperInterface){
        OnlineRecordProxy proxy = proxyThreadLocal.get();
        if(proxy != null){
            return proxy;
        }
        proxy = new OnlineRecordProxy(mapperInterface);
        proxyThreadLocal.set(proxy);
        return proxy;
    }

    public static void remove(){
        proxyThreadLocal.remove();
    }

    public void buildParamterValues(Object[] values){
        if(values == null){
            paramterValues = new Object[0];
        }
        paramterValues = new Object[values.length];
        for(int i = 0; i < values.length; i++){
            paramterValues[i] = values[i];
        }
    }

    public void beforeMethod(Object o, String method){
        if(!isTestFront(method)) {
            return;
        }
        getColaMockito().getContext().setRecording(true);
        ColaMockito colaMockito = initColaMockito();
        colaMockito.getFileDataEngine().clean();
    }

    public void afterMethod(Object o, String method){
        if(!isTestFront(method)) {
            return;
        }

        String fullMethodName = o.getClass().getName() + NOTE_SYMBOL + method;
        ColaTestGenerator generator = new ColaTestGenerator(fullMethodName, ColaTestRecordController.getTemplateSuperClassName());
        generator.generate(paramterValues);

        changeDataEngineFilePath(o.getClass().getName(), method);
        getColaMockito().getFileDataEngine().flushOutputData();
        getColaMockito().getFileDataEngine().flushInputParamsFile();
        getColaMockito().getContext().setRecording(false);
    }

    /**
     * 通过全局ColaMockito.g() 来判断是否开启
     * @return
     */
    private boolean isTestFront(String method) {
        return canRecord(method)
            && ColaMockito.g().getCurrentTestModel().getTestClazz().isAssignableFrom(mapperInterface);
    }

    private ColaMockito initColaMockito() {
        ColaMockito colaMockito = new ColaMockito();
        try {
            colaMockito.getContext().setRecording(ColaMockito.g().getContext().isRecording());
            colaMockito.getContext().setColaTestModelList(
                new ArrayList<>(ColaMockito.g().getContext().getColaTestModelMap().values()));
            colaMockito.getContext().setColaTestMeta(ColaMockito.g().getContext().getColaTestMeta());
            colaMockito.setFileDataEngine(new FileDataEngine(
                DataStoreEnum.JSON_STORE, ColaMockito.g().getFileDataEngine().getSrcResource()));
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
        return colaMockito;
    }

    protected boolean canRecord(String method){
        return recordStarted(method);
    }

    private boolean recordStarted(String method){
        if(getColaMockito() == null){
            return false;
        }
        if(getColaMockito().getCurrentTestModel() == null){
            return false;
        }

        String methodName = getColaMockito().getContext().getColaTestMeta().getDescription().getMethodName();
        return method.contains(methodName);
    }

    protected ColaMockito getColaMockito(){
        return ColaMockito.g();
    }

    private void changeDataEngineFilePath(String className, String methodName){
        Map<String, MockDataFile> repo = getColaMockito().getFileDataEngine().getRepo();
        Map<String, InputParamsFile> inputRepo = getColaMockito().getFileDataEngine().getInputRepo();

        String repoFilePath = FileUtils.getAbbrOfClassName(className) + UNDERLINE + methodName + "Test_" + methodName;
        Map<String, MockDataFile> newRepo = new HashMap<>();
        Map<String, InputParamsFile> newInputRepo = new HashMap<>();
        repo.entrySet().forEach(e->{
            e.getValue().setFileId(repoFilePath);
            newRepo.put(repoFilePath, e.getValue());
        });

        inputRepo.entrySet().forEach(e->{
            String filePath = repoFilePath + INPUT_PARAMS_FILE_SUFFIX;
            e.getValue().setFileId(filePath);
            newInputRepo.put(filePath, e.getValue());
        });

        getColaMockito().getFileDataEngine().setRepo(newRepo);
        getColaMockito().getFileDataEngine().setInputRepo(newInputRepo);
    }

}
