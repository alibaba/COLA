#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.event.handler;

import com.alibaba.sofa.dto.Response;
import com.alibaba.sofa.dto.event.Event;
import com.alibaba.sofa.event.EventHandler;
import com.alibaba.sofa.event.EventHandlerI;

/**
 * @author shawnzhan.zxy
 * @date 2017/11/30
 */
@EventHandler
public class CustomerChangeEventHandler implements EventHandlerI {


    @Override
    public Response execute(Event event) {
        System.out.println("Add your own other logic here");
        return Response.buildSuccess();
    }
}
