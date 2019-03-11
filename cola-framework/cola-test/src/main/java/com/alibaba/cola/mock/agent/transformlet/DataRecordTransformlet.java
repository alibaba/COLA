package com.alibaba.cola.mock.agent.transformlet;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class DataRecordTransformlet extends AbstractTransformlet {
    public DataRecordTransformlet(String className){
        super(className);
    }

    @Override
    public byte[] transform(String className, byte[] classFileBuffer, ClassLoader loader, String config)
        throws Exception {
        return new byte[0];
    }
}
