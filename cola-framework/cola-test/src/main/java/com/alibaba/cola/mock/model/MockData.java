package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/04
 */
public class MockData {
    private String dataId;
    /** 为啥这里需要volatile？*/
    private volatile int curIndex = 0;
    private long start;
    private long end;
    private List<Object> dataList = new ArrayList<>();

    public MockData(String dataId){
        this.dataId = dataId;
    }

    public void put(Object data){
        this.dataList.add(data);
    }

    public Object nextData(){
        return this.dataList.get(curIndex++);
    }

    public boolean hasNext(){
        return curIndex < dataList.size();
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public List<Object> getUsedDataList(){
        return dataList.subList(0, curIndex);
    }
}
