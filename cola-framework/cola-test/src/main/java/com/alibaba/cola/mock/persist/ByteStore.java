package com.alibaba.cola.mock.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.MockData;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.utils.Constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public class ByteStore implements DataStore {

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
        return;
    }

    @Override
    public MockDataFile get(String fileId, String filePath) {
        return loadDataFile(fileId, filePath);
    }

    @Override
    public InputParamsFile getInputParamsFile(String fileId, String filePath) {
        return null;
    }

    private MockDataFile loadDataFile(String fileId, String filePath){
        MockDataFile mockDataFile = new MockDataFile();
        mockDataFile.setFileId(fileId);
        try {
            RandomAccessFile rf = new RandomAccessFile(filePath, "r");
            List<MockData> mockDataList = loadDataFileHead(rf);
            for(MockData mockData : mockDataList){
                rf.seek(mockData.getStart());
                byte[] bs = new byte[Long.valueOf(mockData.getEnd()-mockData.getStart()).intValue()];
                rf.read(bs);

                ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(bs));
                //读出对象
                ArrayList arry =(ArrayList)ois.readObject();
                arry.forEach(p->{
                    mockData.put(p);
                });
                mockDataFile.putMockData(mockData);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mockDataFile;
    }

    private List<MockData> loadDataFileHead(RandomAccessFile rf) throws IOException {
        List<MockData> mockDataList = new ArrayList<>();
        long fileLength = rf.length();
        // 返回此文件中的当前偏移量
        long start = rf.getFilePointer();
        long readIndex = start + fileLength -1;
        String line;
        // 设置偏移量为文件末尾
        rf.seek(readIndex);
        int c = -1;
        while (readIndex > start) {
            c = rf.read();
            String readText = null;
            if (c == '\n' || c == '\r') {
                line = rf.readLine();
                if (line != null) {
                    readText = new String(line.getBytes("ISO-8859-1"));
                }
                if(StringUtils.isBlank(readText)){
                    continue;
                }

                if(Constants.RESPONSE_DATA_DELIMITER.equals(readText)){
                    break;
                }else{
                    MockData mockData = createMockDataHead(readText);
                    mockDataList.add(mockData);
                }
                readIndex--;
            }
            readIndex--;
            rf.seek(readIndex);
        }
        return mockDataList;
    }

    public void saveDataFile(MockDataFile mockDataFile, String filePath) throws Exception {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        System.out.println("======================"+file.getAbsolutePath());
        System.out.println("======================"+file.getPath());
        file.createNewFile();

        saveDataFileBody(mockDataFile, file);
        saveDataFileHead(mockDataFile, file);
    }


    private MockData createMockDataHead(String headLine){
        String[] heads = headLine.split(Constants.RESPONSE_METHOD_DELIMITER);
        String[] limit = heads[1].split(",");
        MockData mockData = new MockData(heads[0]);
        mockData.setEnd(Long.valueOf(limit[1]));
        mockData.setStart(Long.valueOf(limit[0]));
        return mockData;
    }

    private void saveDataFileBody(MockDataFile mockDataFile, File file) throws Exception {
        int start = 0;
        int end = 0;
        FileOutputStream fos = new FileOutputStream(file);
        for(MockData mockData : mockDataFile.getAllMockData()){
            int length = writeObjectToFileStream(fos, mockData.getDataList());
            end = start + length;
            mockData.setStart(start);
            mockData.setEnd(end);
            start = end;
        }
    }

    private int writeObjectToFileStream(FileOutputStream fos, Object data) throws IOException {
        int length = 0;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bo);
        try{
            oos.writeObject(data);
            length = bo.size();
            bo.writeTo(fos);
        }finally {
            oos.close();
        }
        return length;
    }
    private void saveDataFileHead(MockDataFile mockDataFile, File file) throws Exception {

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        //将写文件指针移到文件尾。
        raf.seek(raf.length());
        raf.writeBytes("\r\n");
        raf.writeBytes(Constants.RESPONSE_DATA_DELIMITER);
        raf.writeBytes("\r\n");

        for (MockData mockData : mockDataFile.getAllMockData()) {
            raf.writeBytes(mockData.getDataId()+Constants.RESPONSE_METHOD_DELIMITER);
            raf.writeBytes(mockData.getStart()+","+mockData.getEnd());
            raf.writeBytes("\r\n");
        }
        raf.close();
    }
}
