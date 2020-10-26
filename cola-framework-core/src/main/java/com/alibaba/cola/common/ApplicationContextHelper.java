package com.alibaba.cola.common;

import com.alibaba.cola.exception.framework.BasicErrorCode;
import com.alibaba.cola.exception.SysException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextHelper
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:30 PM
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return  ApplicationContextHelper.applicationContext;
    }

    public static<T> T getBean(Class<T> targetClz){
        T beanInstance = null;
        //优先按type查
        try {
            beanInstance = (T) applicationContext.getBean(targetClz);
        }catch (Exception e){
        }
        //按name查
        if(beanInstance == null){
            String simpleName = targetClz.getSimpleName();
            //首字母小写
            simpleName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            beanInstance = (T) applicationContext.getBean(simpleName);
        }
        if(beanInstance == null){
            new SysException(BasicErrorCode.COLA_ERROR, "Component " + targetClz + " can not be found in Spring Container");
        }
        return beanInstance;
    }
    public static Object getBean(String claz){
        return ApplicationContextHelper.applicationContext.getBean(claz);
    }
}
