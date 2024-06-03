package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.extension.Extensions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


import java.util.Map;

/**
 * ExtensionBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
@Slf4j
@Component
public class ExtensionBootstrap implements ApplicationContextAware {

    @Resource
    private ExtensionRegister extensionRegister;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, ExtensionPointI> extMap = applicationContext.getBeansOfType(ExtensionPointI.class);
        for (ExtensionPointI ext : extMap.values()) {
            if (ext.getClass().isAnnotationPresent(Extension.class)) {
                extensionRegister.doRegistration(ext);
            }else if (ext.getClass().isAnnotationPresent(Extensions.class)){
                extensionRegister.doRegistrationExtensions(ext);
            }else {
                log.error("There is no annotation for @Extension or @Extension on this extension class:{}" , ext.getClass());
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
