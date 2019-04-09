#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.repository;

import java.util.ArrayList;
import java.util.List;

import ${package}.tunnel.database.dataobject.CustomerDO;
import ${package}.tunnel.database.CustomerDBTunnel;
import ${package}.convertor.CustomerConvertor;
import ${package}.domain.customer.CustomerE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.alibaba.cola.repository.RepositoryI;

@Repository
public class CustomerRepository implements RepositoryI{

    @Autowired
    private CustomerDBTunnel customerDBTunnel;

    @Autowired
    private CustomerConvertor customerConvertor;

    public void save(CustomerE customer) {
        customerDBTunnel.create(customerConvertor.entityToData(customer));
    }

}