package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.exceptions.ErrorCodes;

/**
 * @author bei.fengb
 * @since 2018/09/04
 */
public class InputListOfSameNameMethod {
    private String dataId;
    private int curIndex = 0;
    private long start;
    private long end;
    private List<InputParamsOfOneMethod> dataList = new ArrayList<>();

    public InputListOfSameNameMethod(String dataId){
        this.dataId = dataId;
    }

    public void put(InputParamsOfOneMethod data){
        this.dataList.add(curIndex++, data);
    }

    public InputParamsOfOneMethod nextData(){
        if(curIndex >= dataList.size()){
            throw new IndexOutOfBoundsException("index " + curIndex + ", size " + dataList.size()
                + ",refer to " + ErrorCodes.INDEX_OUT_OF_BOUNDS);
        }
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

    public List<InputParamsOfOneMethod> getDataList() {
        return dataList;
    }

    public void setDataList(List<InputParamsOfOneMethod> dataList) {
        this.dataList = dataList;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public List<InputParamsOfOneMethod> getUsedDataList(){
        return dataList.subList(0, curIndex);
    }
}
