package com.alibaba.cola.mock.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.agent.transformlet.AbstractTransformlet;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class ColaTransformer implements ClassFileTransformer {
    private static final byte[] EMPTY_BYTE_ARRAY = {};
    private final List<AbstractTransformlet> transformletList = new ArrayList<AbstractTransformlet>();

    private String config;

    public ColaTransformer(String config){
        this.config = config;
    }

    @Override
    public byte[] transform(ClassLoader loader, String classFile, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
        throws IllegalClassFormatException {
        try {
            // Lambda has no class file, no need to transform, just return.
            if (classFile == null){
                return EMPTY_BYTE_ARRAY;
            }

            final String className = toClassName(classFile);
            for (AbstractTransformlet transformlet : transformletList) {
                //final byte[] bytes = transformlet.doTransform(className, classfileBuffer, loader, config);
                //if (bytes != null){
                //    return bytes;
                //}
            }
        } catch (Throwable t) {
            String msg = "Fail to transform class " + classFile + ", cause: " + t.toString();
            throw new IllegalStateException(msg, t);
        }

        return EMPTY_BYTE_ARRAY;
    }

    private static String toClassName(final String classFile) {
        return classFile.replace('/', '.');
    }
}
