package com.alibaba.cola.mock.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;
import com.alibaba.cola.mock.agent.transformlet.AbstractTransformlet;
import com.alibaba.cola.mock.agent.transformlet.DefaultConstructTransformlet;
import com.alibaba.cola.mock.agent.transformlet.OnlineRecordTransformlet;
import com.alibaba.cola.mock.agent.transformlet.TransformletUtils;

import javassist.CtClass;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class ColaTransformer implements ClassFileTransformer {
    private static final byte[] EMPTY_BYTE_ARRAY = {};
    private static Map<String, byte[]> cacheMap = new ConcurrentHashMap<>();

    private AgentArgs args;

    public ColaTransformer(AgentArgs args){
        this.args = args;
    }

    @Override
    public byte[] transform(ClassLoader loader, String classFile, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classFileBuffer)
        throws IllegalClassFormatException {
        try {
            // Lambda has no class file, no need to transform, just return.
            if (classFile == null){
                return EMPTY_BYTE_ARRAY;
            }

            final String className = toClassName(classFile);
            if(!className.equals(args.getClassName())){
                return classFileBuffer;
            }

            byte[] cacheByteCode = cacheMap.get(args.getKey());
            if(cacheByteCode != null){
                return cacheByteCode;
            }
            cacheByteCode = doTransform(loader, className, classFileBuffer).toBytecode();
            cacheMap.put(args.getKey(), cacheByteCode);
            return cacheByteCode;
        } catch (Throwable t) {
            t.printStackTrace();
            String msg = "Fail to transform class " + classFile + ", cause: " + t.toString();
            throw new IllegalStateException(msg, t);
        }
    }

    private CtClass doTransform(ClassLoader loader, String className, byte[] classFileBuffer) throws Exception {
        CtClass clazz = TransformletUtils.getCtClass(classFileBuffer, loader);
        if(args.getTranslateType().equals(TranslateType.DEFAULT_CONSTRUCTOR)){
            AbstractTransformlet transformlet = new DefaultConstructTransformlet(className);
            clazz = transformlet.doTransform(className, clazz, loader, args);
        }else if(args.getTranslateType().equals(TranslateType.ONLINE_RECORD)){
            AbstractTransformlet transformlet = new OnlineRecordTransformlet(className);
            clazz = transformlet.doTransform(className, clazz, loader, args);
        }
        return clazz;
    }

    public AgentArgs getArgs() {
        return args;
    }

    private static String toClassName(final String classFile) {
        return classFile.replace('/', '.');
    }
}
