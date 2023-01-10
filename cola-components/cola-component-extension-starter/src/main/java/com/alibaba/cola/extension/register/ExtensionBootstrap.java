package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.extension.ExtensionPointI;
import com.alibaba.cola.extension.Extensions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Map;

/**
 * ExtensionBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
@Component
public class ExtensionBootstrap implements ApplicationContextAware {

    @Resource
    private ExtensionRegister extensionRegister;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ExtensionPointI) extension)
        );

        // handle @Extensions annotation
        Map<String, Object> extensionsBeans = applicationContext.getBeansWithAnnotation(Extensions.class);
        extensionsBeans.values().forEach( extension -> extensionRegister.doRegistrationExtensions((ExtensionPointI) extension));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
