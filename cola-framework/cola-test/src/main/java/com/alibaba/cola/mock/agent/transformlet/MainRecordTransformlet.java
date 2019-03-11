package com.alibaba.cola.mock.agent.transformlet;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class MainRecordTransformlet extends AbstractTransformlet {
    private String methodName;
    public MainRecordTransformlet(String className){
        super(className);
    }

    @Override
    public byte[] transform(String className, byte[] classFileBuffer, ClassLoader loader, String config)
        throws Exception {
        final CtClass clazz = TransformletUtils.getCtClass(classFileBuffer, loader);
        CtMethod method = null;
        try {
            method = clazz.getDeclaredMethod(methodName);
        }catch(Exception e){
            method = clazz.getMethod(methodName, "");
        }

        TransformletUtils.doArroundForMethod(method, berforeCode(), afterCode());
        return clazz.toBytecode();
    }

    private String berforeCode(){
        StringBuilder sb = new StringBuilder();
        sb.append("if(ColaMockito.g().getCurrentTestModel() != null						   ");
        sb.append("    && ColaMockito.g().getCurrentTestModel().getTestClazz().isAssignableFrom(this.getClass())){ ");
        sb.append("    ColaMockito.g().getFileDataEngine().clean();						   ");
        sb.append("}												   ");
        return sb.toString();
    }

    private String afterCode(){
        StringBuilder sb = new StringBuilder();
        sb.append("if(!MockHelper.isMonitorMethod(" + methodName + ")){						  ");
        sb.append("    return result;										  ");
        sb.append("}												  ");
        sb.append("												  ");
        sb.append("if(ColaMockito.g().getCurrentTestModel() != null						  ");
        sb.append("    && ColaMockito.g().getCurrentTestModel().getTestClazz().isAssignableFrom(this.getClass())){");
        sb.append("    ColaMockito.g().getFileDataEngine().flush();						  ");
        sb.append("    ColaMockito.g().getFileDataEngine().flushInputParamsFile();				  ");
        sb.append("}												  ");
        return sb.toString();
    }
}
