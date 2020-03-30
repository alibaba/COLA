package com.alibaba.cola.mock.persist;

import com.alibaba.cola.mock.model.InputParamsFile;
import com.alibaba.cola.mock.model.MockDataFile;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public interface DataStore {

    public void save(MockDataFile mockDataFile, String filePath);
    public void save(InputParamsFile inputParamsFile, String filePath);


    public MockDataFile get(String fileId, String filePath);
    public InputParamsFile getInputParamsFile(String fileId, String filePath);

}
