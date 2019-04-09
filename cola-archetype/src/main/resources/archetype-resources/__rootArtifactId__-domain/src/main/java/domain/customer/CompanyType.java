#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer;

/**
 * CompanyType
 *
 * @author Frank Zhang
 * @date 2018-01-08 11:02 AM
 */
public enum CompanyType {
    POTENTIAL,
    INTENTIONAL,
    IMPORTANT,
    VIP;
}
