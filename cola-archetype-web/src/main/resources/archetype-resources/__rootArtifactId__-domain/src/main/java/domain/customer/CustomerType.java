#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer;

/**
 * CustomerType
 *
 * @author Frank Zhang
 * @date 2018-01-08 8:51 AM
 */
public enum CustomerType {
    POTENTIAL,
    INTENTIONAL,
    IMPORTANT,
    VIP;
}
