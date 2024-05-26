#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain;

public class DomainFactory {

    public static <T> T get(Class<T> entityClz){
        return ApplicationContextHelper.getBean(entityClz);
    }

}
