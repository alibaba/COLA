#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.charge;

public enum CallType {
    /**
     * 主叫
     */
    CALLING,
    /**
     * 被叫
     */
    CALLED
}
