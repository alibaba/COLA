#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.test;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import ${package}.api.CustomerServiceI;
import ${package}.common.BizCode;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.domainmodel.Customer;
import ${package}.dto.domainmodel.ErrorCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This is for integration test.
 *
 * Created by fulan.zjf on 2017/11/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerServiceI customerService;


    @Before
    public void setUp() {

    }

    @Test
    public void testCustomerAddSuccess(){
        //1.prepare
        CustomerAddCmd customerAddCmd = new CustomerAddCmd();
        Customer customer = new Customer();
        customer.setCompanyName("NormalName");
        customerAddCmd.setCustomer(customer);

        //2.execute
        Response response = customerService.addCustomer(customerAddCmd);

        //3.assert
        Assert.assertTrue(response.isSuccess());
    }

    @Test(expected = BizException.class)
    public void testCustomerAddCompanyNameConflict(){
        //1.prepare
        CustomerAddCmd customerAddCmd = new CustomerAddCmd();
        Customer customer = new Customer();
        customer.setCompanyName("ConflictCompanyName");
        customerAddCmd.setCustomer(customer);

        //2.execute
        Response response = customerService.addCustomer(customerAddCmd);

        //3.Exception
        //Assert.assertEquals(ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode(), response.getErrCode());

    }
}
