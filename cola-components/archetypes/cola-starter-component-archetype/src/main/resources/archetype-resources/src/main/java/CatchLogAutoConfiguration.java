#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

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
@EnableAspectJAutoProxy
public class CatchLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CatchLogAspect.class)
    public CatchLogAspect initCatchLogAspect() {
        return new CatchLogAspect();
    }
}
