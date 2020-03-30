package com.alibaba.cola.mock.agent.transformlet;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author shawnzhan.zxy
 * @date 2019/05/10
 */
public class OnlineRecordTransformlet extends AbstractTransformlet{

    public OnlineRecordTransformlet(String className) {
        super(className, TranslateType.ONLINE_RECORD);
    }

    @Override
    CtClass transform(String className, CtClass clazz, ClassLoader loader, AgentArgs config) throws Exception {
        CtMethod method = null;
        try {
            method = clazz.getDeclaredMethod(config.getMethodName());
        }catch(Exception e){
            method = clazz.getMethod(config.getMethodName(), "");
        }
        TransformletUtils.doArroundForMethod(clazz, method, berforeCode(method), afterCode(method));
        return clazz;
    }

    private String berforeCode(CtMethod method){
        StringBuilder sb = new StringBuilder();
        sb.append("com.alibaba.cola.mock.agent.proxy.OnlineRecordProxy proxy = com.alibaba.cola.mock.agent.proxy.OnlineRecordProxy.get(this.getClass());");
        sb.append("proxy.buildParamterValues($args);");
        sb.append("proxy.beforeMethod(this,\""+method.getName()+"\");");
        return sb.toString();
    }

    private String afterCode(CtMethod method){
        StringBuilder sb = new StringBuilder();
        sb.append("com.alibaba.cola.mock.agent.proxy.OnlineRecordProxy proxy = com.alibaba.cola.mock.agent.proxy.OnlineRecordProxy.get(this.getClass());");
        sb.append("proxy.afterMethod(this,\""+method.getName()+"\");");
        sb.append("com.alibaba.cola.mock.agent.proxy.OnlineRecordProxy.remove();");
        return sb.toString();
    }
}
