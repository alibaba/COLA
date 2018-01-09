#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.customer.rule.extension;

import ${package}.common.BizCode;
import ${package}.domain.customer.entity.CustomerE;
import ${package}.domain.customer.rule.extensionpoint.CustomerRuleExtPt;
import ${package}.domain.customer.valueobject.SourceType;
import com.alibaba.sofa.exception.BizException;
import com.alibaba.sofa.extension.Extension;

/**
 * CustomerDDRuleExt
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:10 PM
 */
@Extension(bizCode = BizCode.DD)
public class CustomerDDRuleExt implements CustomerRuleExtPt {

    @Override
    public boolean addCustomerCheck(CustomerE customerEntity) {
        if(SourceType.AD == customerEntity.getSourceType()){
            throw new BizException("Sorry, Customer from advertisement can not be added in this period");
        }
        return true;
    }
}
