package com.alibaba.cola.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.model.MockServiceModel;
import com.alibaba.cola.mock.model.StackTree;

import org.junit.runner.Description;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/02
 */
public class ColaMockContext implements Serializable{
    /** 当前测试实例信息*/
    private ColaTestDescription colaTestMeta;
    private StackTree stackTree = new StackTree();
    /** 整体监控列表，容器启动需要一次性将需要mock的类都通过cglib代理*/
    private List<MockServiceModel> monitorList = new ArrayList<>();
    /**
     * 所有测试类的配置元数据
     */
    private Map<Class, ColaTestModel> colaTestModelMap = new HashMap<>();

    /** 本次mock列表，当前测试类需要mock的类，属于monitorList的子集*/
    private List<MockServiceModel> mockList = new ArrayList<>();

    /** 集成测试 spy 全局服务列表*/
    private List<MockServiceModel> spyList = new ArrayList<>();

    /** 是否录入数据*/
    private boolean isRecording = false;


    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public void putMock(MockServiceModel mockServiceModel){
        if(!mockList.contains(mockServiceModel)) {
            mockList.add(mockServiceModel);
        }
    }

    public void putSpy(MockServiceModel mockServiceModel){
        if(!spyList.contains(mockServiceModel)) {
            spyList.add(mockServiceModel);
        }
    }

    public void putMonitorMock(MockServiceModel mockServiceModel){
        if(!monitorList.contains(mockServiceModel)){
            monitorList.add(mockServiceModel);
        }
    }

    public MockServiceModel getMockByMInstance(Object mock){
        return mockList.stream().filter(p->p.getMock() == mock).findFirst().orElse(null);
    }

    public MockServiceModel getMockByIntf(Class intf){
        for(MockServiceModel mockServiceModel : mockList){
            if(mockServiceModel.getInterfaceCls().equals(intf)){
                return mockServiceModel;
            }
        }
        return null;
    }

    public MockServiceModel getMonitorMockByTarget(Object bean){
        return monitorList.stream().filter(p->{
            Class targetCls = bean.getClass();
            if(bean instanceof FactoryBean){
                targetCls = ((FactoryBean)bean).getObjectType();
            }
            if(p.getInterfaceCls().isAssignableFrom(targetCls)){
                return true;
            }
            return false;
        }).findFirst().orElse(null);
    }

    public MockServiceModel getMonitorMockByCls(Class intfClazz){
        return monitorList.stream().filter(p->{
            if(p.getInterfaceCls().equals(intfClazz) || p.getInterfaceCls().isAssignableFrom(intfClazz)){
                return true;
            }
            return false;
        }).findFirst().orElse(null);
    }

    public void clearMockList(){
        mockList.clear();
    }

    public List<Object> getProxyInstanceFromMonitorList(){
        return monitorList.stream().map(p->p.getMock()).collect(Collectors.toList());
    }

    public Map<Class, ColaTestModel> getColaTestModelMap() {
        return colaTestModelMap;
    }

    public void putColaTestModel(ColaTestModel colaTestModel) {
        colaTestModelMap.put(colaTestModel.getTestClazz(), colaTestModel);
    }

    public void setColaTestModelList(List<ColaTestModel> colaTestModelList) {
        colaTestModelMap.putAll(colaTestModelList.stream().collect(Collectors.toMap(ColaTestModel::getTestClazz, v->v)));
    }

    public Description getDescription() {
        return colaTestMeta.getDescription();
    }

    public void clean(){
        setColaTestMeta(null);
        stackTree = new StackTree();
    }

    public void setColaTestMeta(ColaTestDescription colaTestMeta) {
        this.colaTestMeta = colaTestMeta;
    }

    public ColaTestDescription getColaTestMeta() {
        return colaTestMeta;
    }

    public List<MockServiceModel> getMonitorList() {
        return monitorList;
    }

    public Object getTestInstance() {
        return colaTestMeta.getTestInstance();
    }

    public StackTree getStackTree() {
        return stackTree;
    }
}

