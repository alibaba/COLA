package com.alibaba.cola.boot;

import com.alibaba.cola.common.ApplicationContextHelper;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SpringBootstrap
 *
 * @author Frank Zhang
 * @date 2020-06-18 7:55 PM
 */
public class SpringBootstrap {

    public void init(){

        ApplicationContext applicationContext =  ApplicationContextHelper.getApplicationContext();

        Map<String, RegisterI> registers = applicationContext.getBeansOfType(RegisterI.class);

        Map<Class<? extends Annotation>, RegisterI> registerMap = registers.values().stream()
                .collect(Collectors.toMap(RegisterI::registrationAnnotation, Function.identity(), (o, n) -> n));

        registerMap.forEach((annotationClazz, reg) ->
                applicationContext.getBeansWithAnnotation(annotationClazz).values().forEach(reg::doRegistration));
    }
}
