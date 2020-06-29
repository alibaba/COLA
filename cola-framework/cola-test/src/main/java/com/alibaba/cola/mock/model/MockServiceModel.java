package com.alibaba.cola.mock.model;

import java.io.Serializable;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/02
 */
public class MockServiceModel implements Serializable{
    /** 服务接口*/
    private Class<?> interfaceCls;
    /** 服务名*/
    private String serviceName;
    /** 被mock实例*/
    private Object target;
    /** mock后生成的实例*/
    private Object mock;
    private boolean spyMockito = false;

    public MockServiceModel(){}
    public MockServiceModel(Class<?> interfaceCls, String serviceName, Object target, Object mock){
        this.interfaceCls = interfaceCls;
        this.serviceName = serviceName;
        this.target = target;
        this.mock = mock;
    }

    public Class<?> getInterfaceCls() {
        return interfaceCls;
    }

    public void setInterfaceCls(Class<?> interfaceCls) {
        this.interfaceCls = interfaceCls;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getMock() {
        return mock;
    }

    public void setMock(Object mock) {
        this.mock = mock;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    //public boolean isSpyMockito() {
    //    return spyMockito;
    //}

    //public void setSpyMockito(boolean spyMockito) {
    //    this.spyMockito = spyMockito;
    //}

    @Override
    public boolean equals(Object obj) {
        MockServiceModel comp = (MockServiceModel)obj;
        return this.interfaceCls.equals(comp.getInterfaceCls()) && this.serviceName.equals(comp.getServiceName());
    }

    @Override
    public int hashCode() {
        return this.interfaceCls.hashCode() + this.serviceName.hashCode();
    }

    @Override
    public MockServiceModel clone() throws CloneNotSupportedException {
        MockServiceModel model = new MockServiceModel(interfaceCls, serviceName, target, mock);
        return model;
    }
}
