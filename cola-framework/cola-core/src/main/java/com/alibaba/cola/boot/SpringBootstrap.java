package com.alibaba.cola.boot;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.cola.extension.Extension;
import com.alibaba.cola.extension.ExtensionPointI;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * SpringBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
public class SpringBootstrap {

    @Resource
    private ExtensionRegister extensionRegister;

    @Resource
    private EventRegister eventRegister;

    public void init(){
       ApplicationContext applicationContext =  ApplicationContextHelper.getApplicationContext();
        Map<String, Object> extensionBeans = applicationContext.getBeansWithAnnotation(Extension.class);
        extensionBeans.values().forEach(
                extension -> extensionRegister.doRegistration((ExtensionPointI) extension)
        );

        Map<String, Object> eventHandlerBeans = applicationContext.getBeansWithAnnotation(EventHandler.class);
        eventHandlerBeans.values().forEach(
                eventHandler -> eventRegister.doRegistration((EventHandlerI) eventHandler)
        );
    }
}
