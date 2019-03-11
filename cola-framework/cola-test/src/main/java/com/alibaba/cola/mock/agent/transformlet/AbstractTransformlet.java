package com.alibaba.cola.mock.agent.transformlet;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public abstract class AbstractTransformlet {
    private String className;

    public AbstractTransformlet(String className){
        this.className = className;
    }

    abstract byte[] transform(String className, byte[] classFileBuffer, ClassLoader loader, String config) throws Exception;

    byte[] doTransform(String className, byte[] classFileBuffer, ClassLoader loader, String config) throws Exception{
        if(!this.className.equals(className)){
            return null;
        }
        return transform(className, classFileBuffer, loader, config);
    }


}
