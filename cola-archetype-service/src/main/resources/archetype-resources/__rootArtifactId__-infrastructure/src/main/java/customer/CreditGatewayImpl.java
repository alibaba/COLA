#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.customer;

import ${package}.domain.customer.Credit;
import ${package}.domain.customer.gateway.CreditGateway;

public class CreditGatewayImpl implements CreditGateway {
    public Credit getCredit(String customerId){
      return null;
    }
}
