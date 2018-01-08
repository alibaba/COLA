#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.sofa.domain.DomainFactoryI;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.domain.customer.repository.ContactRepository;

@Component
public class CustomerDomainFactory implements DomainFactoryI<CustomerE>{

	@Autowired
	private ContactRepository contactRepository;

	public CustomerE create(){
		return new CustomerE();
	}

}
