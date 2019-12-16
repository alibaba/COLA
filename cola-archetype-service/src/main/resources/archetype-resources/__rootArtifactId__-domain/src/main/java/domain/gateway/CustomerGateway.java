#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.gateway;

import ${package}.domain.customer.Customer;

public interface CustomerGateway {
    public Customer getByById(String customerId);
}
