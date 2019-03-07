package com.alibaba.craftsman.tunnel.rpc.dataobject;

import lombok.Data;

@Data
public class AppMetricDO {
    private String appName;//应用名称
    private int cyclomaticComplexityCount;//圈复杂度超标的数目
    private int duplicatedMethodCount;//重复代码的数目
    private int longMethodCount;//长方法的数目
    private int blockedCodeConductCount;//不符合编码标准的数目
}
