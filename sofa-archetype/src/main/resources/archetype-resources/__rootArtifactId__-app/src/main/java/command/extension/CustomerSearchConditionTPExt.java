#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.command.extension;

import ${package}.command.extensionpoint.CustomerCheckConflictSearchConditionExtPt;
import ${package}.config.BizCode;
import com.alibaba.sofa.extension.Extension;

@Extension(bizCode = BizCode.TP)
public class CustomerSearchConditionTPExt implements CustomerCheckConflictSearchConditionExtPt {

    @Override
    public String getSearchCondition(String condition) {
        return "Trust Pass Search Condition";
    }

}
