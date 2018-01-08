#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.interceptor;

import com.alibaba.sofa.command.CommandInterceptorI;
import com.alibaba.sofa.command.PreInterceptor;
import com.alibaba.sofa.dto.Command;

@PreInterceptor
public class ContextInterceptor implements CommandInterceptorI{

    @Override
    public void preIntercept(Command command) {
        //Set Tenant Context here
        //TenantContext.set("1", "CGS");
    }

}
