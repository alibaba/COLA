package com.alibaba.cola.mock.agent.model;

import org.apache.commons.lang3.StringUtils;

/**
 * @author shawnzhan.zxy
 * @since 2019/04/30
 */
public class AgentArgs {
    private String methodName;
    private String className;
    private TranslateType translateType;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TranslateType getTranslateType() {
        return translateType;
    }

    public void setTranslateType(TranslateType translateType) {
        this.translateType = translateType;
    }

    public String getKey(){
        String mName = "_";
        String cName = "_";
        if(StringUtils.isNotBlank(className)){
            cName = className;
        }
        if(StringUtils.isNotBlank(methodName)){
            mName = methodName;
        }
        return translateType.name() + "_" + cName + "_" + mName;
    }
}
