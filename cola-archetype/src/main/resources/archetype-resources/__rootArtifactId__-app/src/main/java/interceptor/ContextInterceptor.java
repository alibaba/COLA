#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.interceptor;

import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PreInterceptor;
import com.alibaba.cola.context.Context;
import com.alibaba.cola.dto.Command;
import ${package}.context.DemoContent;

@PreInterceptor
public class ContextInterceptor implements CommandInterceptorI{

    @Override
    public void preIntercept(Command command) {
        Context<DemoContent> context = new Context();
        DemoContent content = new DemoContent();
        content.setUserId("testUserId");
        context.setContent(content);

        if(command.getContext() != null){
            context.setBizCode(command.getContext().getBizCode());
        }

        command.setContext(context);

    }

}
