package com.alibaba.cola.mock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ColaMockConfig;
import com.alibaba.cola.mock.annotation.ExcludeCompare;
import com.alibaba.cola.mock.scan.TypeFilter;
import com.alibaba.cola.mock.utils.FileUtils;

/**
 * @author shawnzhan.zxy
 * @since 2018/09/24
 * 测试类的配置元数据
 */
public class ColaTestModel implements Serializable{
    Class<?> testClazz;
    transient ColaMockConfig colaMockConfig;
    List<TypeFilter> mockFilters = new ArrayList<>();
    List<TypeFilter> dataManufactureFilters = new ArrayList<>();
    Map<String, Object> dataMaps;
    /**
     * 单测方法，单测方法配置
     */
    Map<String, ExcludeCompare> noNeedCompareConfigMap = new HashMap<String, ExcludeCompare>();


    public void putCurrentTestMethodConfig(String testMethodName, ExcludeCompare currentMethodConfig){
        noNeedCompareConfigMap.put(testMethodName,currentMethodConfig);
    }

    public ExcludeCompare getCurrentTestMethodConfig(String testMethodName){
        return noNeedCompareConfigMap.get(testMethodName);
    }

    public Map<String, ExcludeCompare> getNoNeedCompareConfigMap() {
        return noNeedCompareConfigMap;
    }

    public Class<?> getTestClazz() {
        return testClazz;
    }

    public void setTestClazz(Class<?> testClazz) {
        this.testClazz = testClazz;
    }

    public ColaMockConfig getColaMockConfig() {
        return colaMockConfig;
    }

    public void setColaMockConfig(ColaMockConfig colaMockConfig) {
        this.colaMockConfig = colaMockConfig;
    }

    public <T> T getDataMap(String key){
        if(dataMaps == null){
            dataMaps = ColaMockito.g().loadDataMaps(FileUtils.getAbbrOfClassName(testClazz.getName()));
        }
        return (T)this.dataMaps.get(key);
    }

    /**
     * 获取dataMap
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getDataMap4Strict(String key){
        T result = getDataMap(key);
        if(result == null && !this.dataMaps.containsKey(key)){
            throw new RuntimeException(String.format("%s dataMap not exists!", key));
        }
        return result;
    }

    public void saveDataMap(String key, Object value){
        if(dataMaps == null){
            dataMaps = ColaMockito.g().loadDataMaps(testClazz.getName());
        }
        dataMaps.put(key, value);
        ColaMockito.g().getDataMapStore().save(dataMaps, testClazz.getName());
    }

    public void addMockFilter(TypeFilter filter) {
        this.mockFilters.add(filter);
    }

    public void addDataManufactureFilter(TypeFilter filter) {
        this.dataManufactureFilters.add(filter);
    }

    public List<TypeFilter> getTypeFilters() {
        return mockFilters;
    }

    public boolean matchMockFilter(Class clazz){
        for(TypeFilter filter : mockFilters){
            if(filter.match(clazz)){
                return true;
            }
        }
        return false;
    }

    public boolean matchDataManufactureFilter(Class clazz){
        for(TypeFilter filter : dataManufactureFilters){
            if(filter.match(clazz)){
                return true;
            }
        }
        return false;
    }
}
