#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.event.handler;

import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;
import ${package}.dto.domainevent.CustomerCreatedEvent;

/**
 * Handle customer created event
 *
 * @author frankzhang
 * @date 2019/04/09
 */
@EventHandler
public class CustomerCreatedHandler implements EventHandlerI<CustomerCreatedEvent> {

    private Logger logger = LoggerFactory.getLogger(CustomerCreatedHandler.class);

    @Override
    public void execute(CustomerCreatedEvent event) {
        logger.debug("Sync new customer to Search");
        logger.debug("Logging system operation for new customer");
    }
}
