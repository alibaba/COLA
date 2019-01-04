#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.repository;

import java.util.ArrayList;
import java.util.List;

import ${package}.tunnel.database.dataobject.CustomerDO;
import ${package}.tunnel.database.CustomerDBTunnel;
import ${package}.domain.customer.convertor.CustomerDomainConvertor;
import ${package}.domain.customer.entity.CustomerE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.alibaba.cola.repository.RepositoryI;

@Repository
public class CustomerRepository implements RepositoryI{

    @Autowired
    private CustomerDBTunnel customerDBTunnel;

    @Autowired
    private CustomerDomainConvertor customerDomainConvertor;

    public void persist(CustomerE customer) {
        customerDBTunnel.create(customerDomainConvertor.entityToData(customer));
    }

}
