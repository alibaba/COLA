package com.alibaba.cola.mock.model;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author shawnzhan.zxy
 * @since 2018/10/18
 */
public class ServiceModel {
    private String serviceName;
    private Class serviceCls;

    public ServiceModel(String serviceName, Class serviceCls){
        this.serviceName = serviceName;
        this.serviceCls = serviceCls;
    }

    public boolean isFactory(){
        return FactoryBean.class.isAssignableFrom(serviceCls);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Class getServiceCls() {
        return serviceCls;
    }

    public void setServiceCls(Class serviceCls) {
        this.serviceCls = serviceCls;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        ServiceModel serviceModel = (ServiceModel)o;
        if(this.getServiceName().equals(serviceModel.getServiceName()) && this.getServiceCls().equals(serviceModel.getServiceCls())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.getServiceName().hashCode() + this.getServiceCls().getName().hashCode();
    }
}
