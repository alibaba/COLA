#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.repository;

import java.util.Set;
import org.springframework.stereotype.Repository;
import com.alibaba.cola.repository.RepositoryI;
import ${package}.domain.customer.entity.ContactE;

@Repository
public class ContactRepository implements RepositoryI {

}
