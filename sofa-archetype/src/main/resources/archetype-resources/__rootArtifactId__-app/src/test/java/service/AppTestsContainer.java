#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.sofa.logger.LoggerFactory;
import com.alibaba.sofa.pandora.test.TestsContainer;

public class AppTestsContainer {

    public static void main(String[] args) {
        LoggerFactory.activateSysLogger();
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");          
        ((AbstractApplicationContext) context).start();
        TestsContainer.start(context);
    }
}
