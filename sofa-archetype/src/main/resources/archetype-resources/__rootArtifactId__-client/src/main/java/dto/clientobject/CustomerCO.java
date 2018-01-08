#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dto.clientobject;

import com.alibaba.sofa.dto.ClientObject;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerCO extends ClientObject {
    private String customerId;
    private String memberId;
    private String customerName;
    private CustomerType customerType;

    @NotEmpty
    private String companyName;
    @NotEmpty
    private String source;  //advertisement, p4p, RFQ, ATM


    
}
