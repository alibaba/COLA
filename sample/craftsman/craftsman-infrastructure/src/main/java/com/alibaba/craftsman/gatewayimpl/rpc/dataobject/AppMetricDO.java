package com.alibaba.craftsman.gatewayimpl.rpc.dataobject;


public class AppMetricDO {
    private String appName;//应用名称
    private int cyclomaticComplexityCount;//圈复杂度超标的数目
    private int duplicatedMethodCount;//重复代码的数目
    private int longMethodCount;//长方法的数目
    private int blockedCodeConductCount;//不符合编码标准的数目

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getCyclomaticComplexityCount() {
        return cyclomaticComplexityCount;
    }

    public void setCyclomaticComplexityCount(int cyclomaticComplexityCount) {
        this.cyclomaticComplexityCount = cyclomaticComplexityCount;
    }

    public int getDuplicatedMethodCount() {
        return duplicatedMethodCount;
    }

    public void setDuplicatedMethodCount(int duplicatedMethodCount) {
        this.duplicatedMethodCount = duplicatedMethodCount;
    }

    public int getLongMethodCount() {
        return longMethodCount;
    }

    public void setLongMethodCount(int longMethodCount) {
        this.longMethodCount = longMethodCount;
    }

    public int getBlockedCodeConductCount() {
        return blockedCodeConductCount;
    }

    public void setBlockedCodeConductCount(int blockedCodeConductCount) {
        this.blockedCodeConductCount = blockedCodeConductCount;
    }
}
