package com.alibaba.cola.mock.agent.transformlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

/**
 * @author shawnzhan.zxy
 * @date 2018/11/12
 */
public class TransformletUtils {

    static CtClass getCtClass(final byte[] classFileBuffer, final ClassLoader classLoader) throws IOException {
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

    public static void doArroundForMethod(CtMethod method, String beforeCode, String afterCode)
        throws CannotCompileException {
        String renamedMethodName = renamedMethodName(method);
        final CtClass clazz = method.getDeclaringClass();
        final CtMethod new_method = CtNewMethod.copy(method, clazz, null);

        // rename original method, and set to private method(avoid reflect out renamed method unexpectedly)
        method.setName(renamedMethodName);
        method.setModifiers(method.getModifiers()
            & ~Modifier.PUBLIC /* remove public */
            & ~Modifier.PROTECTED /* remove protected */
            | Modifier.PRIVATE /* add private */);

        // set new method implementation
        final String code = "{\n" +
            beforeCode + "\n" +
            "Object result = " + renamedMethodName + "($$);\n" +
            afterCode + "\n";
        new_method.setBody(code);
        clazz.addMethod(new_method);
    }

    static String renamedMethodName(CtMethod method) {
        return "original$" + method.getName() + "$ColaTest";
    }

}
