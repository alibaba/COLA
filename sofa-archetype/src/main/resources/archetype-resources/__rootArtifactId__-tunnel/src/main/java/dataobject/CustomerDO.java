#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dataobject;

import com.alibaba.sofa.repository.DataObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerDO implements DataObject {
    private String customerId;
    private String memberId;
    private String globalId;
    private String companyName;
    private String source;
    private String companyType;
}
