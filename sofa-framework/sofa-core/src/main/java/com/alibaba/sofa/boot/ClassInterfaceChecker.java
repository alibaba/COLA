package com.alibaba.sofa.boot;

import com.alibaba.common.lang.ArrayUtil;
import com.alibaba.common.lang.StringUtil;

/**
 * @author fulan.zjf
 * @date 2017/12/21
 */
public class ClassInterfaceChecker {

    public static boolean check(Class<?> targetClz, String expectedName){
        //If it is not Class, just return false
        if(targetClz.isInterface()){
            return false;
        }

        Class[] interfaces = targetClz.getInterfaces();
        if (ArrayUtil.isEmpty(interfaces))
            return false;
        for (Class intf : interfaces) {
            String intfSimpleName = intf.getSimpleName();
            if (StringUtil.equals(intfSimpleName, expectedName))
                return true;
        }
        return false;
    }
}
