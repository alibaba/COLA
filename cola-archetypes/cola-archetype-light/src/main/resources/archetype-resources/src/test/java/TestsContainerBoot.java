#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.alibaba.cola.test.TestsContainer;

public class TestsContainerBoot {
    public static void main(String[] args) {
        TestsContainer.start();
    }
}
