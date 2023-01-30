/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.extension;

/**
 * Extension Coordinate(扩展坐标) is used to uniquely position an Extension
 * @author fulan.zjf 2017-11-05
 */
public class ExtensionCoordinate {

    private final String extensionPointName;
    private final String bizScenarioUniqueIdentity;

    /**
     * Wrapper
     */
    private Class<?> extensionPointClass;
    private BizScenario bizScenario;

    public Class getExtensionPointClass() {
        return extensionPointClass;
    }

    public BizScenario getBizScenario() {
        return bizScenario;
    }

    public static ExtensionCoordinate valueOf(Class<?> extPtClass, BizScenario bizScenario){
        return new ExtensionCoordinate(extPtClass, bizScenario);
    }

    public ExtensionCoordinate(Class<?> extPtClass, BizScenario bizScenario){
        this.extensionPointClass = extPtClass;
        this.extensionPointName = extPtClass.getName();
        this.bizScenario = bizScenario;
        this.bizScenarioUniqueIdentity = bizScenario.getUniqueIdentity();
    }

    public ExtensionCoordinate(String extensionPoint, String bizScenario){
        this.extensionPointName = extensionPoint;
        this.bizScenarioUniqueIdentity = bizScenario;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bizScenarioUniqueIdentity == null) ? 0 : bizScenarioUniqueIdentity.hashCode());
        result = prime * result + ((extensionPointName == null) ? 0 : extensionPointName.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }

        if (obj == null) {
          return false;
        }

        if (getClass() != obj.getClass()) {
          return false;
        }

        ExtensionCoordinate other = (ExtensionCoordinate) obj;
        if (bizScenarioUniqueIdentity == null) {
            if (other.bizScenarioUniqueIdentity != null) {
              return false;
            }
        } else if (!bizScenarioUniqueIdentity.equals(other.bizScenarioUniqueIdentity)) {
          return false;
        }
        if (extensionPointName == null) {
          return other.extensionPointName == null;
        } else {
          return extensionPointName.equals(other.extensionPointName);
        }
    }

    @Override
    public String toString() {
        return "ExtensionCoordinate [extensionPointName=" + extensionPointName + ", bizScenarioUniqueIdentity=" + bizScenarioUniqueIdentity + "]";
    }

}
