#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring Boot应用的入口类
 *
 * @author Frank Zhang
 */
@ImportResource(locations = {"classpath*:app-boot-start.xml"})
@SpringBootApplication(scanBasePackages = {"${groupId}"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
