package com.alibaba.cola.mock.persist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.cola.mock.model.HeaderVersion;
import com.alibaba.cola.mock.model.InputListOfSameNameMethod;
import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.InputParamsOfOneMethod;
import com.alibaba.cola.mock.model.MockData;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public class JsonStore implements DataStore {

    @Override
    public void save(MockDataFile mockDataFile, String filePath) {
        try {
            saveDataFile(mockDataFile, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(InputParamsFile inputParamsFile, String filePath) {
        try {
            saveInputParamsFile(inputParamsFile, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MockDataFile get(String fileId, String filePath) {
        return loadDataFile(fileId, filePath);
    }

    @Override
    public InputParamsFile getInputParamsFile(String fileId, String filePath) {
        return loadInputParamsFile(fileId, filePath);
    }

    private void saveDataFile(MockDataFile mockDataFile, String filePath) throws Exception {
        File file = FileUtils.reCreateFile(filePath);

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        writeLine(raf, getHeader(mockDataFile.getHeader()));
        for(MockData mockData : mockDataFile.getAllMockData()){
            //// TODO: 2018/9/14 未知原因，特殊处理 
            if(mockData.getDataId().endsWith("_toString")){
                continue;
            }
            writeLine(raf, mockData.getDataId());
            writeLine(raf, Constants.RESPONSE_METHOD_DELIMITER  + "------------------------");
            writeLine(raf, checkAndToJson(mockData.getDataList()));
            writeLine(raf, Constants.RESPONSE_DATA_DELIMITER    + "========================");
        }
        raf.close();
    }

    private void saveInputParamsFile(InputParamsFile inputParamsFile, String filePath) throws Exception {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        writeLine(raf, getHeader(inputParamsFile.getHeader()));
        for(InputListOfSameNameMethod inputLists : inputParamsFile.getAllInputRecords()){
            //// TODO: 2018/9/14 未知原因，特殊处理
            if(inputLists.getDataId().endsWith("_toString")){
                continue;
            }
            writeLine(raf, inputLists.getDataId());
            writeLine(raf, Constants.METHOD_PARAM_DELIMITER);

            writeLine(raf, checkAndToJson(to2DimensionArr(inputLists.getDataList())));
            writeLine(raf, Constants.METHOD_METHOD_DELIMITER);
        }
        raf.close();
    }

    private Object[] to2DimensionArr(List<InputParamsOfOneMethod> list){
        List<Object[]> arr = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return arr.toArray();
        }

        for(InputParamsOfOneMethod input : list){
            arr.add(input.getInutParams());
        }
        return arr.toArray();
    }

    public static void main(String[] args){

        InputParamsFile inputParamsFile = new InputParamsFile();
        inputParamsFile.setFileId("ContactChangeListener_test");

        List<Object> objList1 = new ArrayList<>();
        objList1.add("i m a string");
        objList1.add(new TestA());
        objList1.add(null);



        InputParamsOfOneMethod inputParam = new InputParamsOfOneMethod();
        inputParam.setInutParams(objList1.toArray());

        List<Object> objList2 = new ArrayList<>();
        objList2.add("i m a string");
        objList2.add(new TestA());
        objList2.add(null);



        InputParamsOfOneMethod inputParam2 = new InputParamsOfOneMethod();
        inputParam2.setInutParams(objList2.toArray());


        JsonStore js = new JsonStore();
        inputParamsFile.putData("accountCacheTunnel_queryByAccount", inputParam);

        inputParamsFile.putData("accountCacheTunnel_queryByAccount", inputParam2);
        for(InputListOfSameNameMethod inputLists : inputParamsFile.getAllInputRecords()){
            if(inputLists.getDataId().endsWith("_toString")){
                continue;
            }

            System.out.println( JSONArray.toJSONString(js.to2DimensionArr(inputLists.getDataList()), SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));
           // System.out.println(JSONArray.toJSONString(inputParamsFile.getAllInputRecords().get(0).getDataList(), SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));
           // System.out.println(Constants.METHOD_METHOD_DELIMITER);
        }

        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        String jsonArr = "[\n"
            + "\t[\n"
            + "        \"i m a string\",\n"
            + "            {\n"
            + "                \"@type\":\"com.alibaba.cola.mock.persist.JsonStore$TestA\",\n"
            + "            \"field1\":\"111\",\n"
            + "            \"field2\":\"222\"\n"
            + "\t\t},\n"
            + "        null\n"
            + "\t],\n"
            + "\t[\n"
            + "        \"i m a string\",\n"
            + "            {\n"
            + "                \"@type\":\"com.alibaba.cola.mock.persist.JsonStore$TestA\",\n"
            + "            \"field1\":\"111\",\n"
            + "            \"field2\":\"222\"\n"
            + "\t\t},\n"
            + "        null\n"
            + "\t]\n"
            + "]";


        JSONArray arr = JSONArray.parseArray(jsonArr);

        Object[] tempParams = new Object[arr.size()];
        Object[]  inputParams = ((JSONArray)arr.get(0)).toArray(tempParams);




       // JSONArray arr = JSON.parseArray(JSONArray.toJSONString(inputParamsFile.getAllInputRecords().get(0).getDataList(), SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));

        System.out.println(1);
    }
    static class  TestA{
        private String field1 ="111";
        private String field2 ="222";

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    static  class TestB{
        private String field1 ="333";
        private String field2 ="444";
        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    private String getHeader(HeaderVersion headerVersion) throws IOException {
        if(headerVersion == null){
            headerVersion = new HeaderVersion(Constants.VERSION, false);
        }
        return JSONObject.toJSONString(headerVersion);
    }

    private MockDataFile loadDataFile(String fileId, String filePath){
        MockDataFile mockDataFile = new MockDataFile();
        mockDataFile.setFileId(fileId);
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filePath, "r");
            String line;
            MockData mockData = null;
            boolean startBody = false;
            boolean startHead = true;
            mockDataFile.setHeader(readHeadVersion(rf));
            while((line = readLine(rf)) != null){
                if(StringUtils.isBlank(line)){
                    continue;
                }
                if(!line.startsWith(Constants.RESPONSE_METHOD_DELIMITER) && startHead){
                    mockData = new MockData(line);
                    mockDataFile.putMockData(mockData);
                    startHead = false;
                }else{
                    startBody = true;
                }
                if(startBody) {
                    mockData.setDataList(loadDataBody(rf));
                    startBody = false;
                    startHead = true;
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(rf != null){
                try {
                    rf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mockDataFile;
    }


    private InputParamsFile loadInputParamsFile(String fileId, String filePath){
        InputParamsFile inputParamsFile = new InputParamsFile();
        inputParamsFile.setFileId(fileId);
        RandomAccessFile rf = null;
        try {

            rf = new RandomAccessFile(filePath, "r");
            String line;
            InputListOfSameNameMethod inputListOfSameNameMethod = null;
            boolean startBody = false;
            boolean startHead = true;
            HeaderVersion header =  readHeadVersion(rf);
            if(!validateVersion(header)){
                throw new RuntimeException("version is not match,currentVersion is:  fileVersion is");
            }
            inputParamsFile.setHeader(header);

            while((line = readLine(rf)) != null){
                if(StringUtils.isBlank(line)){
                    continue;
                }
                if(!Constants.METHOD_METHOD_DELIMITER.equals(line) && startHead){
                    inputListOfSameNameMethod = new InputListOfSameNameMethod(line);
                    inputParamsFile.putParamsOfSameNameMethod(inputListOfSameNameMethod);
                    startHead = false;
                }else{
                    startBody = true;
                }
                if(startBody) {
                    inputListOfSameNameMethod.setDataList((List<InputParamsOfOneMethod>)loadInputParamsBody(rf));
                    startBody = false;
                    startHead = true;
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(rf != null){
                try {
                    rf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return inputParamsFile;
    }

    private boolean validateVersion(HeaderVersion header){
        return true;
    }



    private List<Object> loadDataBody(RandomAccessFile rf) throws Exception{
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = readLine(rf)) != null){
            if(StringUtils.isBlank(line)){
                continue;
            }
            if(line.startsWith(Constants.RESPONSE_DATA_DELIMITER)){
                break;
            }
            sb.append(line);
        }
        List<Object> dataList = null;
        dataList = parseJson(sb.toString());
        return dataList;
    }

    /**
     * 注意空字符串的解析
     * @param rf
     * @return
     * @throws Exception
     */
    private List<InputParamsOfOneMethod> loadInputParamsBody(RandomAccessFile rf) throws Exception{
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = readLine(rf)) != null){
            if(StringUtils.isBlank(line)){
                continue;
            }
            if(Constants.METHOD_METHOD_DELIMITER.equals(line)){
                break;
            }
            sb.append(line);
        }

        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        /*

        JSONArray arr =  JSONArray.parseArray(sb.toString());//.toJavaList((Object[]).class
        */

        JSONArray arr = JSONArray.parseArray(sb.toString());

        //Object[] tt = JSONArray.toJavaObject()arr.get(0);

        List<InputParamsOfOneMethod> inputsList = new ArrayList<>();
        if(arr.size() == 0){
            return inputsList;
        }


        for(int i=0; i<arr.size();i++){
            Object[] tempParams = new Object[0];
            Object[]  inputParams = ((JSONArray)arr.get(i)).toArray(tempParams);
            InputParamsOfOneMethod inputParamsOfOneMethod = new InputParamsOfOneMethod();
            inputParamsOfOneMethod.setInutParams(inputParams);
            inputsList.add(inputParamsOfOneMethod);
        }


        return inputsList;

    }


    private void writeLine(RandomAccessFile raf, String line) throws IOException {
        raf.write(line.getBytes("utf-8"));
        raf.write("\r\n".getBytes("utf-8"));
    }

    private HeaderVersion readHeadVersion(RandomAccessFile raf) throws IOException {
        String header = readLine(raf);
        return JSON.parseObject(header, HeaderVersion.class);
    }

    private String readLine(RandomAccessFile raf) throws IOException {
        String line = raf.readLine();
        if(StringUtils.isBlank(line)){
            return line;
        }
        line = new String(line.getBytes("ISO-8859-1"), "utf-8");
        line = StringUtils.trim(line);
        return line;
    }

    private String checkAndToJson(Object data){
        String json = JSONArray.toJSONString(data, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat);
        checkJson(json);
        return json;
    }

    private void checkJson(String json){
        parseJson(json);
    }

    private<T> T parseJson(String json){
        T data = null;
        try{
            data = (T)JSON.parse(json);
        }catch (JSONException e){
            //if(e.getMessage().indexOf("autoType is not support") < 0){
            //    throw e;
            //}
            throw new RuntimeException("无法反序列化,请确保序列化对象满足POJO,同时 存在无参构造!json=>" + json, e);
        }
        return data;
    }

}
