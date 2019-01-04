#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.tunnel.database.dataobject;

import com.alibaba.cola.tunnel.DataObject;
import lombok.Data;

@Data
public class CustomerDO extends DataObject {
    private String customerId;
    private String memberId;
    private String globalId;
    private String companyName;
    private String source;
    private String companyType;
}
