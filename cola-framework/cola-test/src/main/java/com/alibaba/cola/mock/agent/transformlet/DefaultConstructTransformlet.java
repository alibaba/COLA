package com.alibaba.cola.mock.agent.transformlet;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * @author shawnzhan.zxy
 * @date 2019/04/30
 */
public class DefaultConstructTransformlet extends AbstractTransformlet{

    public DefaultConstructTransformlet(String className){
        super(className, TranslateType.DEFAULT_CONSTRUCTOR);
    }

    @Override
    CtClass transform(String className, CtClass clazz, ClassLoader loader, AgentArgs config) throws Exception {
        CtConstructor ctConstructor = null;
        try {
            ctConstructor = clazz.getDeclaredConstructor(new CtClass[] {});
        }catch (NotFoundException e){
        }
        if(ctConstructor != null){
            return clazz;
        }
        ctConstructor = new CtConstructor(new CtClass[]{}, clazz);
        clazz.addConstructor(ctConstructor);
        //CtConstructor constructor = new CtConstructor(null, clazz);
        //constructor.setBody("{}");
        //clazz.addConstructor(constructor);
        return clazz;
    }
}
