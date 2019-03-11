package com.alibaba.cola.mock.utils;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/12
 */
public class Constants {
    public final static String VERSION  = "1.0.0";
    public final static String RESPONSE_DATA_DELIMITER = "===";
    public final static String RESPONSE_METHOD_DELIMITER = "---";
    public final static String SERVICE_LIST_DELIMITER = "_@_";
    public final static String UNDERLINE = "_";
    public final static String DOT = ".";
    /** 入参文件后缀*/
    public final static String INPUT_PARAMS_FILE_SUFFIX = "_inputParams";
    /** colamock 代理标识*/
    public final static String COLAMOCK_PROXY_FLAG = "ByColaMockWithCGLIB";
    public final static String INNER_BEAN_NAME = "inner bean";
    public final static String NOTE_SYMBOL = "#";


    public final static String METHOD_PARAM_DELIMITER = "=================method_params_separator================";
    public final static String PARAM_PARAM_DELIMITER = "=========param_params_separator=========";
    public final static String METHOD_METHOD_DELIMITER = "=======================different_method_separator=============================";

    /** colamockconfig格式化模板*/
    public final static String COLAMOCKCONFIG_TEMPLATE = "@ColaMockConfig(mocks={%s})";

}