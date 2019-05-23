package com.alibaba.cola.mock.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/03
 */
public class FileUtils {

    public static final String PACKAGE_SPLITER = ".";
    public static final String PACKAGE_SPLITER_REGEP = "\\.";

    public static File createFileIfNotExists(String srcResource){
        File file = new File(srcResource);
        File dir = file.getParentFile();
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File reCreateFile(String filePath) throws IOException {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    public static String convertPackage2Path(String packagePath){
        packagePath = packagePath.replaceAll("\\.", "/");
        return packagePath + "/";
    }

    public static String appendSlashToURILast(String srcResource){
        srcResource.replaceAll("\\\\", "/");
        if(!srcResource.endsWith("/")){
            srcResource = srcResource + "/";
        }
        return srcResource;
    }

    public static void createDirectory(String srcResource){
        String dirPath = srcResource;
        if(!dirPath.endsWith("/")){
            dirPath = dirPath.substring(0, dirPath.lastIndexOf("/"));
        }
        File file = new File(dirPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static String getDefaultDirectory4MockFile(){
        String dir = FileUtils.class.getResource("/").getPath().replaceAll("target/test-classes", "src/test/resources/mockfile");
        return appendSlashToURILast(dir);
    }

    public static String getDefaultDirectory4TestFile(){
        String dir = FileUtils.class.getResource("/").getPath().replaceAll("target/test-classes", "src/test/java");
        return appendSlashToURILast(dir);
    }

    /**
     * 重建文件
     *    删除+新建
     * @param file
     * @return
     */
    public static File resoreFile(File file){
        String filePath = file.getAbsolutePath();
        if(file != null && file.exists()){
            if(!file.delete()){
                throw new RuntimeException("clean is error,delete file error!");
            }
        }
        return createFileIfNotExists(filePath);
    }

    public static boolean isFileExists(String filePath){
        File f = new File(filePath);
        if(f.exists()){
            return true;
        }
        return false;
    }

    public static InputStream getTestClassTemplate(){
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("testclass-template.ftl");
        }catch (Exception e) {
        }
        if(is == null){
            is = FileUtils.class.getResourceAsStream("/META-INF/testclass-template.ftl");
        }
        return is;
    }

    /**
     * 录制文件名缩写，原本com.alibaba.Test_execute->c.a.Test_execute
     * @param originClassName
     * @return
     */
    public static String getAbbrOfClassName(String originClassName){

        String[] classNameFragment = originClassName.split(PACKAGE_SPLITER_REGEP);

        StringBuilder shortName = new StringBuilder();
        for(int i=0; i<classNameFragment.length; i++){
            if(i< classNameFragment.length-1){
                shortName.append(new String(new char[]{classNameFragment[i].charAt(0)})).append(PACKAGE_SPLITER);
            }else{
                shortName.append(classNameFragment[i]);
            }
        }
        return shortName.toString();

    }
}
