package com.alibaba.cola.mock.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.model.MockData;
import com.alibaba.cola.mock.model.MockDataFile;

import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @date 2019/02/12
 */
public class ColaMockConfigPrettyHelper {
    ColaTestModel colaTestModel;

    public ColaMockConfigPrettyHelper(ColaTestModel colaTestModel){
        this.colaTestModel = colaTestModel;
    }

    public String pretty(){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("mocks", assembleMockConfig());
        dataMap.put("dataManufactures", assembleDataManufacureConfig());
        Template t;
        StringWriter sw = new StringWriter();
        try {
            t = new Template(null, Constants.COLAMOCKCONFIG_TEMPLATE, null);
            t.process(dataMap, sw);
            sw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private String assembleMockConfig(){
        if(colaTestModel.getColaMockConfig() == null){
            return StringUtils.EMPTY;
        }
        Class[] mocks = colaTestModel.getColaMockConfig().mocks();
        Set<String> clazzNameSet = getAllActualMockClasses();
        if(mocks.length != clazzNameSet.size()){
            return CommonUtils.format2ColaConfigClazz(clazzNameSet);
        }
        return StringUtils.EMPTY;
    }

    private String assembleDataManufacureConfig(){
        if(colaTestModel.getColaMockConfig() == null){
            return StringUtils.EMPTY;
        }
        Class[] dataManufactures = colaTestModel.getColaMockConfig().dataManufactures();
        if(dataManufactures.length == 0){
            return StringUtils.EMPTY;
        }
        List<String> dataManufactureClazz = new ArrayList<>();
        for(Class dm : dataManufactures){
            dataManufactureClazz.add(dm.getSimpleName());
        }
        return CommonUtils.format2ColaConfigClazz(dataManufactureClazz);
    }

    private Set<String> getAllActualMockClasses(){
        Set<String> clazzNameSet = new HashSet<>();
        if(!ColaMockito.g().getFileDataEngine().isExsitsMockDataFileByFileId(ColaMockito.g().getCurrentTestUid())){
            return clazzNameSet;
        }
        MockDataFile mockDataFile = ColaMockito.g().getFileDataEngine().getMockDataFileByFileId(ColaMockito.g().getCurrentTestUid());

        List<MockData> mockDataLst = mockDataFile.getAllMockData();
        for(MockData mockData : mockDataLst){
            clazzNameSet.add(mockData.getDataId().split(Constants.UNDERLINE)[0]);
        }
        return clazzNameSet;
    }
}
