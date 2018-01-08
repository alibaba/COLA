#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import ${package}.TestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Diamond的测试
 * 
 * @author fulan.zjf
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestApplication.class })
public class ConfigTest {

}
