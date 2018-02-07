#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 单元测试的Spring Boot配置类
 * @author fulan.zjf
 */
@ImportResource(locations = {"classpath*:app-boot-${artifactId}.xml"})
@SpringBootApplication(scanBasePackages = {"${groupId}"})
@PropertySource(value = { "classpath:test.properties" })
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
