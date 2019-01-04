package com.alibaba.cola.test;

import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * SysLoggerStarter
 *
 * For testing purpose, make sure SysLogger is activated before any bean creation
 *
 * @author Frank Zhang
 * @date 2019-01-03 10:21 AM
 */
@Component
public class SysLoggerStarter implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //if you want to use sysLogger, it should be activated before bean creation
        LoggerFactory.activateSysLogger();
    }
}
