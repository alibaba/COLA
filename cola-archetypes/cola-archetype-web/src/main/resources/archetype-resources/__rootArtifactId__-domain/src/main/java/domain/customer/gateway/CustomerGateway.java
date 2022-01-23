#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.gateway;

import ${package}.domain.customer.Customer;

public interface CustomerGateway {
    Customer getByById(String customerId);
}
