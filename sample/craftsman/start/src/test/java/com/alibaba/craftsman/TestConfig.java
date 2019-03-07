package com.alibaba.craftsman;

import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * TestConfig,  {@link com.alibaba.craftsman.config.ColaConfig}
 *
 * @author Frank Zhang
 * @date 2018-08-08 12:33 PM
 */
@Configuration
@ComponentScan(basePackages = {"com.alibaba.cola", "com.alibaba.craftsman"})
public class TestConfig {
    Logger logger = LoggerFactory.getLogger(TestConfig.class);

    public TestConfig() {
        logger.debug("Spring container is booting");
    }

}