#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.command.extension;

import ${package}.command.extensionpoint.CustomerCheckConflictSearchConditionExtPt;
import ${package}.common.BizCode;
import com.alibaba.sofa.extension.Extension;

@Extension(bizCode = BizCode.CGS)
public class CustomerSearchConditionCGSExt implements CustomerCheckConflictSearchConditionExtPt {

    @Override
    public String getSearchCondition(String condition) {
        return "CGS Search Condition";
    }

}
