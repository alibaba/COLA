#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.charge.chargeplan;

public enum ChargePlanType {
    /**
     * 基础套餐
     */
    BASIC,
    /**
     * 固定时常套餐
     */
    FIXED_TIME,
    /**
     * 家庭套餐
     */
    FAMILY
}
