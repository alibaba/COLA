#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.app;

import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * TestConfig, use @Configuration to replace xml
 *
 * COLA framework initialization is configured in {@link ${package}.config.ColaConfig}
 *
 * @author Frank Zhang
 * @date 2018-08-08 12:33 PM
 */
@Configuration
@ComponentScan(basePackages = {"com.alibaba.cola", "${package}"})
public class TestConfig {

    public TestConfig() {
        LoggerFactory.activateSysLogger();
        Logger logger = LoggerFactory.getLogger(TestConfig.class);
        logger.debug("Spring container is booting");
    }

}