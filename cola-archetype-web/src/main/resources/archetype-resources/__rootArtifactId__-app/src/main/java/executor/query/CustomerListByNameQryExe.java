#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.executor.query;

import com.alibaba.cola.command.Command;
import com.alibaba.cola.command.QueryExecutorI;
import com.alibaba.cola.dto.MultiResponse;
import ${package}.dto.CustomerListByNameQry;
import ${package}.dto.domainmodel.Customer;
import java.util.ArrayList;
import java.util.List;

@Command
public class CustomerListByNameQryExe implements QueryExecutorI<MultiResponse<Customer>, CustomerListByNameQry> {

    @Override
    public MultiResponse<Customer> execute(CustomerListByNameQry cmd) {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customer.setCustomerName("Frank");
        customerList.add(customer);
        return MultiResponse.ofWithoutTotal(customerList);
    }
}
