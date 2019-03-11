package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/04
 */
public class MockDataFile {
    private String fileId;
    private HeaderVersion header;
    private Map<String, MockData> mockDataMap = new LinkedHashMap<>();

    public MockDataFile(){}

    public void putData(String dataId, Object data){
        MockData mockData = mockDataMap.get(dataId);
        if(mockData == null){
            mockData = new MockData(dataId);
            mockDataMap.put(dataId, mockData);
        }
        mockData.put(data);
    }

    public void putMockData(MockData mockData){
        MockData oldMockData = mockDataMap.get(mockData.getDataId());
        if(oldMockData != null){
            return;
        }
        mockDataMap.put(mockData.getDataId(), mockData);
    }

    public List<MockData> getAllMockData(){
        return new ArrayList<>(mockDataMap.values());
    }

    public Object getData(String dataId){
        MockData mockData = mockDataMap.get(dataId);
        if(mockData == null){
            return null;
        }
        return mockData.nextData();
    }

    public<T> T getData(String dataId, Class<T> dataType){
        Object data = getData(dataId);
        if(data == null){
            return null;
        }
        return (T)data;
    }

    public boolean contain(String dataId){
        return mockDataMap.containsKey(dataId);
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public HeaderVersion getHeader() {
        return header;
    }

    public void setHeader(HeaderVersion header) {
        this.header = header;
    }

    public void cleanNoUseData(){
        Map<String, MockData> newMockDataMap = new LinkedHashMap<>();
        mockDataMap.entrySet().forEach(entry->{
            MockData mockData = entry.getValue();
            if(!mockData.hasNext()){
                newMockDataMap.put(entry.getKey(), mockData);
                return;
            }
            if(isNeverUse(mockData)){
                return;
            }
            mockData.setDataList(mockData.getUsedDataList());
            newMockDataMap.put(entry.getKey(), mockData);
        });
        mockDataMap = newMockDataMap;
    }

    private boolean isNeverUse(MockData mockData){
        if(mockData.getDataList().size() == 0 || mockData.getCurIndex() == 0){
            return true;
        }
        return false;
    }
}
