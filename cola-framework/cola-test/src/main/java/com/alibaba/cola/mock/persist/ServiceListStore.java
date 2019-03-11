package com.alibaba.cola.mock.persist;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public class ServiceListStore {
    private static final Logger logger = LoggerFactory.getLogger(ServiceListStore.class);
    private static final String FILE_NAME = "service.list";
    private File file;
    private String dir;
    public ServiceListStore() {
        this(FileUtils.getDefaultDirectory4MockFile());
    }

    public ServiceListStore(String srcResource){
        this.dir = srcResource;
        this.file = FileUtils.createFileIfNotExists(getFilePath());
    }

    public void clean(){
        this.file = FileUtils.resoreFile(this.file);
    }

    public void save(String content) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            writeLine(raf, content);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(List<String> contentLst){
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            contentLst.forEach(p -> {
                try {
                    writeLine(raf, p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> load(){
        List<String> lines = new ArrayList<>();
        try {
            RandomAccessFile rf = new RandomAccessFile(file, "r");
            String line;
            while((line = rf.readLine()) != null){
                lines.add(line);
            }
            rf.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private void writeLine(RandomAccessFile raf, String line) throws IOException {
        raf.writeBytes(line);
        raf.writeBytes("\r\n");
    }

    private String getFilePath(){
        this.dir = FileUtils.appendSlashToURILast(dir);
        return this.dir + FILE_NAME;
    }
}
