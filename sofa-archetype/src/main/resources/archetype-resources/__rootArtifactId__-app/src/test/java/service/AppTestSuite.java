#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AppTestSuite
 *
 * @author Frank Zhang
 * @date 2018-01-08 9:49 AM
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
        CustomerServiceTest.class,
})
public class AppTestSuite {

}
