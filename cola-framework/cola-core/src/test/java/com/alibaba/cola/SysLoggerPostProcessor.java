package com.alibaba.cola;

import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * SysLoggerPostProcessor
 *
 * @author Frank Zhang
 * @date 2018-08-19 2:42 PM
 */
@Component
public class SysLoggerPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LoggerFactory.activateSysLogger();
    }
}
