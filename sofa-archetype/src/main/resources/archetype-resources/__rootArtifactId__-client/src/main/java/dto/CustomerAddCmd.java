#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dto;

import ${package}.dto.clientobject.CustomerCO;
import com.alibaba.sofa.dto.Command;
import lombok.Data;

@Data
public class CustomerAddCmd extends Command{

    private CustomerCO customer;
    
}
