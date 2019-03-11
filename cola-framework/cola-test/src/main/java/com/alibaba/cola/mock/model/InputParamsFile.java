package com.alibaba.cola.mock.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * bei.fengb 2018-09-24 08:21
 */
public class InputParamsFile {
    private String fileId;
    private HeaderVersion header;
    /**
     * key = 被mock类名+方法  value = 对应方法的入参（若出现多次，则入参是list列表，同名方法放在一起）
     */
    private Map<String, InputListOfSameNameMethod> method2Inputs = new LinkedHashMap<>();
    /**
     * 单个testcase中所有mock的方法的入参
     */
    private List<InputListOfSameNameMethod> inputsOfAllMockedMethods = new ArrayList<>();


    public InputParamsFile(){}

    public void putData(String classNamePlusMethodName, InputParamsOfOneMethod params){
        InputListOfSameNameMethod inputList = method2Inputs.get(classNamePlusMethodName);
        if(inputList == null){
            inputList = new InputListOfSameNameMethod(classNamePlusMethodName);
            method2Inputs.put(classNamePlusMethodName, inputList);
        }
        inputList.put(params);
    }

    public void putParamsOfSameNameMethod(InputListOfSameNameMethod inputRecord){
        InputListOfSameNameMethod existInputRecord = method2Inputs.get(inputRecord.getDataId());
        if(existInputRecord != null){
            return;
        }
        method2Inputs.put(inputRecord.getDataId(), inputRecord);
    }

    public List<InputListOfSameNameMethod> getAllInputRecords(){
        return new ArrayList<>(method2Inputs.values());
    }

    public InputParamsOfOneMethod getCurrentInputOfOneMethod(String classNamePlusMethodName){
        InputListOfSameNameMethod inputRecord = method2Inputs.get(classNamePlusMethodName);
        if(inputRecord == null){
            return null;
        }
        return inputRecord.nextData();
    }

    public boolean contain(String dataId){
        return method2Inputs.containsKey(dataId);
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
        Map<String, InputListOfSameNameMethod> newMethod2Inputs = new LinkedHashMap<>();
        method2Inputs.entrySet().forEach(entry->{
            InputListOfSameNameMethod mockData = entry.getValue();
            if(!mockData.hasNext()){
                newMethod2Inputs.put(entry.getKey(), mockData);
                return;
            }
            if(mockData.getDataList().size() == 0 || mockData.getCurIndex() == 0){
                return;
            }
            mockData.setDataList(mockData.getUsedDataList());
            newMethod2Inputs.put(entry.getKey(), mockData);
        });
        method2Inputs = newMethod2Inputs;
    }
}
