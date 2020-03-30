package com.alibaba.cola.mock.persist;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.cola.mock.model.ColaTestModel;
import com.alibaba.cola.mock.scan.RegexPatternTypeFilter;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public class ColaTestModelStore {
    private static final Logger logger = LoggerFactory.getLogger(ColaTestModelStore.class);
    private File file;
    private String srcResource;

    public ColaTestModelStore(String srcResource){
        this.srcResource = srcResource;
        this.file = FileUtils.createFileIfNotExists(this.srcResource);
    }

    public List<ColaTestModel> load(){
        List<ColaTestModel> models = null;
        try {
            RandomAccessFile rf = new RandomAccessFile(file, "r");
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = rf.readLine()) != null){
                line = line.trim();
                if(StringUtils.isBlank(line)){
                    continue;
                }
                if(line.startsWith(Constants.NOTE_SYMBOL)){
                    continue;
                }
                sb.append(line);
            }
            JSONArray array = JSONArray.parseArray(sb.toString());
            models = resolveColatestModel(array);
            rf.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return models;
    }

    private List<ColaTestModel> resolveColatestModel(JSONArray array){
        List<ColaTestModel> models = new ArrayList<>();
        if(array == null){
            return models;
        }
        array.stream().forEach(p->{
            JSONObject data = (JSONObject)p;
            ColaTestModel colaTestModel = new ColaTestModel();
            try {
                colaTestModel.setTestClazz(Class.forName(data.getString("class")));
            } catch (ClassNotFoundException e) {
                logger.error("resolveColatestModel error!", e);
            }
            //List<String> methodList = Arrays.asList(data.getString("methods").split(","));
            //methodList.stream().forEach(method->{
            //    colaTestModel.putCurrentTestMethodConfig(method, null);
            //});

            JSONArray recordPointList = data.getJSONArray("recordPoints");
            recordPointList.stream().forEach(point->{
                colaTestModel.addMockFilter(new RegexPatternTypeFilter(point.toString()));
            });
            models.add(colaTestModel);
        });
        return models;
    }
}
