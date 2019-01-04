/*
 * Copyright 2017 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.cola.extension;

import lombok.Data;

/**
 * Extension Coordinate(扩展点坐标) is used to uniquely position a Extension
 * @author fulan.zjf 2017-11-05
 */
@Data
public class ExtensionCoordinate {
    
    private String extensionPoint;
    private String bizCode;

    /**
     * @param extensionPoint
     * @param bizCode
     */
    public ExtensionCoordinate(String extensionPoint, String bizCode){
        super();
        this.extensionPoint = extensionPoint;
        this.bizCode = bizCode;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bizCode == null) ? 0 : bizCode.hashCode());
        result = prime * result + ((extensionPoint == null) ? 0 : extensionPoint.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ExtensionCoordinate other = (ExtensionCoordinate) obj;
        if (bizCode == null) {
            if (other.bizCode != null) return false;
        } else if (!bizCode.equals(other.bizCode)) return false;
        if (extensionPoint == null) {
            if (other.extensionPoint != null) return false;
        } else if (!extensionPoint.equals(other.extensionPoint)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ExtensionCoordinate [extensionPoint=" + extensionPoint + ", bizCode=" + bizCode + "]";
    }
    
}
