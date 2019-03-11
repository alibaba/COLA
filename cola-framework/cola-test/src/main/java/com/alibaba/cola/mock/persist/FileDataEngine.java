package com.alibaba.cola.mock.persist;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.MockData;
import com.alibaba.cola.mock.model.MockDataFile;
import com.alibaba.cola.mock.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件存储引擎
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class FileDataEngine {
    private static final Logger logger = LoggerFactory.getLogger(FileDataEngine.class);
    private String srcResource;
    private Map<String, MockDataFile> repo = new ConcurrentHashMap<>();
    private Map<String, InputParamsFile> inputRepo = new ConcurrentHashMap<>();
    private DataStore dataStore;

    public FileDataEngine(DataStoreEnum dataStore){
        this(dataStore, FileUtils.getDefaultDirectory4MockFile());
    }

    public FileDataEngine(DataStoreEnum dataStore, String srcResource){
        this.srcResource = FileUtils.appendSlashToURILast(srcResource);
        initStore(dataStore);
        FileUtils.createDirectory(this.srcResource);
    }

    public MockDataFile createAndGetMockDataFileByFileId(String fileId){
        MockDataFile mockDataFile = repo.get(fileId);
        if(mockDataFile != null){
            return mockDataFile;
        }
        mockDataFile = new MockDataFile();
        mockDataFile.setFileId(fileId);
        repo.put(fileId, mockDataFile);
        return mockDataFile;
    }


    public InputParamsFile createAndGetInputParamsFileByFileId(String fileId){
        InputParamsFile inputParamsFile = inputRepo.get(fileId);
        if(inputParamsFile != null){
            return inputParamsFile;
        }
        inputParamsFile = new InputParamsFile();
        inputParamsFile.setFileId(fileId);
        inputRepo.put(fileId, inputParamsFile);
        return inputParamsFile;
    }

    public boolean isExsitsMockDataFileByFileId(String fileId){
        URL url = this.getClass().getResource("/mockfile/" + fileId);
        if(url == null){
            return false;
        }
        return true;
    }

    public MockDataFile getMockDataFileByFileId(String fileId){
        MockDataFile mockDataFile = repo.get(fileId);
        if(mockDataFile != null){
            return mockDataFile;
        }
        URL url = this.getClass().getResource("/mockfile/" + fileId);
        if(url == null){
            throw new RuntimeException("filepath=/mockfile/" + fileId + " cannot find");
        }
        String filePath = url.getPath();
        mockDataFile = dataStore.get(fileId, filePath);
        printMockDataList(mockDataFile);
        repo.put(fileId, mockDataFile);
        return mockDataFile;
    }

    public InputParamsFile getInputParamsFileByFileId(String fileId){
        InputParamsFile inputParamsFile = inputRepo.get(fileId);
        if(inputParamsFile != null){
            return inputParamsFile;
        }
        URL url = this.getClass().getResource("/mockfile/" + fileId);
        if(url == null){
            throw new RuntimeException("filepath=/mockfile/" + fileId + " cannot find");
        }
        String filePath = url.getPath();
        inputParamsFile = dataStore.getInputParamsFile(fileId, filePath);
        inputRepo.put(fileId, inputParamsFile);
        return inputParamsFile;
    }

    public void flush(){
        if(repo.size() == 0){
            logger.warn("repo is null, check record point!");
            return;
        }
        repo.entrySet().forEach(entry->{
            try {
                this.dataStore.save(entry.getValue(), getFileName(entry.getValue()));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        repo.clear();
    }

    public void clean(){
        repo.clear();
        inputRepo.clear();
    }

    /**
     * 检查是否还有剩余数据未被使用
     * @return
     */
    public boolean validHasRemainData(){
        boolean remain = false;
        for(MockDataFile mockDataFile : repo.values()){
            boolean singleMockDataFileRemain = false;
            if(checkMockDataFileHasRemainData(mockDataFile)){
                singleMockDataFileRemain = true;
            }
            //System.out.println(mockDataFile.getFileId() + "==" + mockDataFile.getHeader());
            if(!mockDataFile.getHeader().getInitialized()){
                if(singleMockDataFileRemain) {
                    logger.info("cleanNoUseData filePath=" + mockDataFile.getFileId());
                    mockDataFile.cleanNoUseData();
                }
                mockDataFile.getHeader().setInitialized(true);
                dataStore.save(mockDataFile, getFileName(mockDataFile));
            }else if(singleMockDataFileRemain){
                logger.info("filePath=" + mockDataFile.getFileId());
                remain = true;
            }
        }

        for(InputParamsFile paramsFile : inputRepo.values()){
            if(paramsFile.getHeader().getInitialized()){
                continue;
            }
            paramsFile.cleanNoUseData();
            paramsFile.getHeader().setInitialized(true);
            dataStore.save(paramsFile, getFileName(paramsFile));
        }
        return remain;
    }

    public void flushInputParamsFile(){
        inputRepo.entrySet().forEach(entry->{
            try {
                this.dataStore.save(entry.getValue(), getFileName(entry.getValue()));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        inputRepo.clear();
    }

    private void initStore(DataStoreEnum dataStore){
        if(DataStoreEnum.JSON_STORE.equals(dataStore)){
            this.dataStore = new JsonStore();
        }else{
            this.dataStore = new ByteStore();
        }
    }

    private void printMockDataList(MockDataFile mockDataFile){
        if(mockDataFile.getAllMockData() == null){
            return;
        }
        mockDataFile.getAllMockData().forEach(p->logger.info("mockData=" + p.getDataId()));
    }

    public String getFileName(MockDataFile mockDataFile){
        return srcResource + mockDataFile.getFileId();
    }

    public String getFileName(InputParamsFile inputParamsFile){
        return srcResource + inputParamsFile.getFileId();
    }

    public String getSrcResource() {
        return srcResource;
    }

    private boolean checkMockDataFileHasRemainData(MockDataFile mockDataFile){
        boolean remain = false;
        for(MockData mockData : mockDataFile.getAllMockData()){
            if(!mockData.hasNext()) {
                continue;
            }
            remain = true;
            logger.warn(mockData.getDataId() + " has remain data,this mockData not used!");
        }
        return remain;
    }
}
