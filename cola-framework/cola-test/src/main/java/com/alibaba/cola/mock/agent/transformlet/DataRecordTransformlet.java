package com.alibaba.cola.mock.agent.transformlet;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;

import javassist.CtClass;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class DataRecordTransformlet extends AbstractTransformlet {
    public DataRecordTransformlet(String className, TranslateType type){
        super(className, type);
    }

    @Override
    public CtClass transform(String className, CtClass clazz, ClassLoader loader, AgentArgs config)
        throws Exception {
        return clazz;
    }
}
