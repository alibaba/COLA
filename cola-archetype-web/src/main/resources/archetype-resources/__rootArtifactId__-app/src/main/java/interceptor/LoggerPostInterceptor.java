#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.interceptor;

import com.alibaba.cola.command.CommandInterceptorI;
import com.alibaba.cola.command.PostInterceptor;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import com.alibaba.fastjson.JSON;

@PostInterceptor
public class LoggerPostInterceptor implements CommandInterceptorI{

    Logger logger = LoggerFactory.getLogger(LoggerPostInterceptor.class);

    @Override
    public void postIntercept(Command command, Response response) {
         logger.debug("End processing, Respons is "+ JSON.toJSONString(response));
    }

}
