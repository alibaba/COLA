package com.alibaba.cola.mock.runner;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.scan.FilterManager;

/**
 * @author shawnzhan.zxy
 * @since 2019/01/04
 */
public abstract class AbstractColaTest {
    protected ColaMockito colaMockito;

    public AbstractColaTest(ColaMockito colaMockito){
        this.colaMockito = colaMockito;
    }

    public void init(ColaTestDescription desc){
        validate(desc.getTestInstance());
        initTest(desc);
        colaMockito.getContext().clearMockList();
        scanMock();
    }

    protected void initTest(ColaTestDescription desc){
        ColaMockito.g().getContext().setColaTestMeta(desc);
    }

    private void scanMock(){
        FilterManager filterManager = new FilterManager();
        if(colaMockito.getCurrentTestModel() == null){
            //此错误一般是把单元测试和集成测试搞混了，比如本来是做单元测试，但是colaconfig 却忘了些UNIT
            throw new RuntimeException("not ready ColaTestModel,please check unit test or integrate test!");
        }
        filterManager.addAll(colaMockito.getCurrentTestModel().getTypeFilters());
        ColaMockito.g().getContext().getMonitorList().forEach(p->{
            if(filterManager.match(p.getInterfaceCls())){
                //ColaMockito.g().getContext().putMock(p);
                colaMockito.getContext().putMock(p);
            }
        });
    }

    private boolean validate(Object testInstance){
        ColaMockConfig colaMockConfig = testInstance.getClass().getAnnotation(ColaMockConfig.class);
        //if(colaMockConfig == null){
        //    throw new RuntimeException("ColaMockConfig cannot null");
        //}
        return true;
    }
}
