package com.alibaba.cola.domain;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @ Description   :
 * @ Author        :  Frank Zhang
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Configuration
public class DomainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApplicationContextHelper.class)
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }
}
