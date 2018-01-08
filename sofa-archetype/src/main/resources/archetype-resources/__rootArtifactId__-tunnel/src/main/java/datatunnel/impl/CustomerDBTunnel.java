#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.datatunnel.impl;

import ${package}.dataobject.CustomerDO;
import ${package}.datatunnel.CustomerTunnelI;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public class CustomerDBTunnel implements CustomerTunnelI {

    @Override
    public CustomerDO create(CustomerDO customerDo) {
        return new CustomerDO();
    }
    
    @Override
    public void update(CustomerDO customerDo) {
        
    }
    
    @Override
    public CustomerDO get(String id) {
        CustomerDO customerDo = new CustomerDO();//just for demo
        return customerDo;
    }

    @Override
    public List<CustomerDO> findByCriteria(String... params) {
        return null;
    }

	@Override
	public List<CustomerDO> getByEntity(CustomerDO arg0) {
		return null;
	}

}
