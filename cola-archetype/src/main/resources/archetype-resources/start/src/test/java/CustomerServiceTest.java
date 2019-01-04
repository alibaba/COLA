#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.alibaba.cola.context.Context;
import com.alibaba.cola.dto.Response;
import ${package}.api.CustomerServiceI;
import ${package}.common.BizCode;
import ${package}.common.exception.ErrorCode;
import ${package}.context.DemoContent;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.clientobject.CustomerCO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This is for integration test.
 *
 * Created by fulan.zjf on 2017/11/29.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CustomerServiceTest {

    @Autowired
    private CustomerServiceI customerService;

    private Context<DemoContent> context;

    @Before
    public void setUp() {
        context = new Context<>();
    }
    @Test
    public void testCustomerAddSuccess(){
        //1.prepare
        CustomerAddCmd customerAddCmd = new CustomerAddCmd();
        context.setBizCode(BizCode.BIZ_ONE);
        customerAddCmd.setContext(context);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("NormalName");
        customerAddCmd.setCustomerCO(customerCO);

        //2.execute
        Response response = customerService.addCustomer(customerAddCmd);

        //3.assert
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testCustomerAddCompanyNameConflict(){
        //1.prepare
        CustomerAddCmd customerAddCmd = new CustomerAddCmd();
        context.setBizCode(BizCode.BIZ_ONE);
        customerAddCmd.setContext(context);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("ConflictCompanyName");
        customerAddCmd.setCustomerCO(customerCO);

        //2.execute
        Response response = customerService.addCustomer(customerAddCmd);

        //3.assert
        Assert.assertEquals(ErrorCode.B_CUSTOMER_companyNameConflict.getErrCode(), response.getErrCode());

    }
}
