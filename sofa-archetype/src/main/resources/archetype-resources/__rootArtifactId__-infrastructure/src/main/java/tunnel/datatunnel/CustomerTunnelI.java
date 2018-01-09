#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.tunnel.datatunnel;

import java.util.List;

import com.alibaba.sofa.repository.DataTunnel;
import ${package}.tunnel.dataobject.CustomerDO;

public interface CustomerTunnelI extends DataTunnel<CustomerDO>{

    public List<CustomerDO> findByCriteria(String... params);

}
