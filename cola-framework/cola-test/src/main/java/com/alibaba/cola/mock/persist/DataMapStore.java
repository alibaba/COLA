package com.alibaba.cola.mock.persist;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.cola.mock.utils.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawnzhan.zxy
 * @since 2018/11/03
 */
public class DataMapStore {
    private static final Logger logger = LoggerFactory.getLogger(DataMapStore.class);
    private static final String FILE_SUFFIX = "_dataMap";

    private String dir;

    public DataMapStore(){
        this(FileUtils.getDefaultDirectory4MockFile());
    }

    public DataMapStore(String dir){
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        this.dir = FileUtils.appendSlashToURILast(dir);
    }

    public void save(Map<String, Object> data, String fileName) {
        try {
            //先检查json的可用性，再重写文件
            String json = JsonUtils.checkAndToJson(data);
            File file = FileUtils.reCreateFile(getFilePath(fileName));
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.write(json.getBytes("utf-8"));
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> load(String fileName){
        String filePath = getFilePath(fileName);
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if(!FileUtils.isFileExists(filePath)){
                return dataMap;
            }
            RandomAccessFile rf = new RandomAccessFile(filePath, "r");
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = readLine(rf)) != null){
                line = line.trim();
                if(StringUtils.isBlank(line)){
                    continue;
                }
                if(line.startsWith(Constants.NOTE_SYMBOL)){
                    continue;
                }
                sb.append(line);
            }
            dataMap = (Map<String, Object>)JSON.parse(sb.toString());
            //第一层的map 不能加@type
            //dataMap = JSONObject.parseObject(sb.toString());
            rf.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataMap;
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

    private String getFilePath(String fileName){
        return this.dir + fileName + FILE_SUFFIX;
    }
}
