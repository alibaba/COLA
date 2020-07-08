package com.alibaba.craftsman.gatewayimpl.rpc.dataobject;


public class BugMetricDO {
    private int bugCount; //缺陷数量
    private long checkInCodeCount; //check in的代码量

    public int getBugCount() {
        return bugCount;
    }

    public void setBugCount(int bugCount) {
        this.bugCount = bugCount;
    }

    public long getCheckInCodeCount() {
        return checkInCodeCount;
    }

    public void setCheckInCodeCount(long checkInCodeCount) {
        this.checkInCodeCount = checkInCodeCount;
    }
}
