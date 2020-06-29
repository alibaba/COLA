package com.alibaba.cola.mock.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.model.ColaTestModel;

import static com.alibaba.cola.mock.utils.Constants.COLAMOCK_PROXY_FLAG;

/**
 * 栈检索器
 * @author shawnzhan.zxy
 * @since 2019/05/22
 */
public class StackSearcher {

    /**
     * 是否第一个mock点
     * @param colaTestModel
     * @return
     */
    public static boolean isTopMockPoint(ColaTestModel colaTestModel, Class mockClass, String mockMethod){
        RuntimeException exception = new RuntimeException();
        try {
            StackTraceElement stackTraceElement = findTopMockPoint(colaTestModel, exception.getStackTrace());
            if(stackTraceElement == null){
                return false;
            }
            if(mockClass.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))
                && mockMethod.equals(stackTraceElement.getMethodName())){
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static StackTraceElement findTopMockPoint(ColaTestModel colaTestModel, StackTraceElement[] stackTraceElements)
        throws ClassNotFoundException {
        for(int i = stackTraceElements.length - 1; i > 0; i--){
            StackTraceElement element = stackTraceElements[i];
            if(isConstantsExcludeStack(element)){
                continue;
            }
            if(colaTestModel.matchMockFilter(Class.forName(element.getClassName()))){
                return element;
            }
        }
        return null;
    }

    public static StackTraceElement[] getBusinessStack(ColaTestModel colaTestModel, StackTraceElement[] stackTraceElements){
        List<StackTraceElement> stacks = new ArrayList<>();
        for(StackTraceElement element : stackTraceElements){
            if(!isConstantsExcludeStack(element)
                && isMatchMockFilter(colaTestModel, element.getClassName())){
                stacks.add(element);
            }
        }
        return stacks.toArray(new StackTraceElement[]{});
    }

    private static boolean isMatchMockFilter(ColaTestModel colaTestModel, String className){
        if(!className.contains(COLAMOCK_PROXY_FLAG)){
            return true;
        }
        try {
            return colaTestModel.matchMockFilter(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isConstantsExcludeStack(StackTraceElement element){
        if(element.getClassName().indexOf("com.alibaba.framework") >= 0
            || element.getClassName().indexOf("org.junit") >= 0
            || element.getClassName().indexOf("org.springframework") >= 0
            || element.getClassName().indexOf("java.") >= 0
            || element.getClassName().indexOf("sun.") >= 0
            || element.getClassName().indexOf("com.intellij") >= 0
            || element.getClassName().indexOf("com.taobao.pandora.boot") >= 0
            || element.getClassName().indexOf("org.mockito") >= 0
            ){
            return true;
        }
        return false;
    }
}
