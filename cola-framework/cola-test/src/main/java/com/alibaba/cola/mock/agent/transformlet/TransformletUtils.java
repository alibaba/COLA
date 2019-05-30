package com.alibaba.cola.mock.agent.transformlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;

import com.alibaba.cola.mock.agent.model.AgentArgs;
import com.alibaba.cola.mock.agent.model.TranslateType;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.TemplateBuilder;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class TransformletUtils {

    public static CtClass getCtClass(final byte[] classFileBuffer, final ClassLoader classLoader) throws IOException {
        final ClassPool classPool = new ClassPool(true);
        if (classLoader == null) {
            classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
        } else {
            classPool.appendClassPath(new LoaderClassPath(classLoader));
        }

        final CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();
        return clazz;
    }

    public static void doArroundForMethod(CtClass clazz, CtMethod method, String beforeCode, String afterCode)
        throws CannotCompileException, NotFoundException {
        method.insertBefore(beforeCode);
        method.insertAfter(afterCode);
    }

    public static void doArroundForCopyNewMethod(CtClass clazz, CtMethod method, String beforeCode, String afterCode)
        throws CannotCompileException, NotFoundException {
        String renamedMethodName = renamedMethodName(method);
        final CtMethod new_method = CtNewMethod.copy(method, clazz, null);

        // rename original method, and set to private method(avoid reflect out renamed method unexpectedly)
        method.setName(renamedMethodName);
        method.setModifiers(method.getModifiers()
            & ~Modifier.PUBLIC /* remove public */
            & ~Modifier.PROTECTED /* remove protected */
            | Modifier.PRIVATE /* add private */);

        TemplateBuilder builder = new TemplateBuilder(Constants.AGENT_NEW_METHOD_TEMPALTE);
        builder.addVar("beforeCode", beforeCode)
            .addVar("afterCode", afterCode)
            .addVar("renamedMethodName", renamedMethodName);
        if(new_method.getReturnType() != null){
            builder.addVar("isReturn", true);
        }
        String code = builder.build();
        new_method.setBody(code);
        clazz.addMethod(new_method);
    }

    static String renamedMethodName(CtMethod method) {
        return "original$" + method.getName() + "$ColaTest";
    }

    static void injectFlagField(CtClass clazz, AgentArgs config) throws Exception{
        CtField ctField = new CtField(CtClass.intType, "cola_" + config.getKey(), clazz);
        clazz.addField(ctField);
    }

    static boolean existsFlagField(CtClass clazz, AgentArgs config) {
        CtField ctField = null;
        try {
            ctField = clazz.getDeclaredField("cola_" + config.getKey());
        } catch (NotFoundException e) {
            return false;
        }
        if(ctField == null){
            return false;
        }
        return true;
    }
}
