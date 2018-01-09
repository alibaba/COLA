#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.repository;

import java.util.ArrayList;
import java.util.List;

import ${package}.tunnel.dataobject.CustomerDO;
import ${package}.tunnel.datatunnel.CustomerTunnelI;
import ${package}.domain.customer.convertor.CustomerConvertor;
import ${package}.domain.customer.entity.CustomerE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.alibaba.sofa.repository.RepositoryI;

@Repository
public class CustomerRepository implements RepositoryI{

    @Autowired
    private CustomerTunnelI customerDBTunnel;

    @Autowired
    private CustomerConvertor customerConvertor;

    public void persist(CustomerE customer) {
        customerDBTunnel.create(customerConvertor.entityToData(customer));
    }
    
    public List<CustomerE> findByCriteria(String... params){
        List<CustomerDO> customerDos = customerDBTunnel.findByCriteria(params);
        List<CustomerE> customerDs = new ArrayList<>();
        for (CustomerDO customerDo : customerDos) {
            //customerDs.add(customerConvertor.toDomainObject(customerDo));
        }
        return customerDs;
    }
}
