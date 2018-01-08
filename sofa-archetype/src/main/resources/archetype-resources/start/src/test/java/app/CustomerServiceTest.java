#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app;

import ${package}.TestApplication;
import ${package}.config.AppConstants;
import ${package}.config.BizCode;
import ${package}.dto.CustomerCheckConflictCmd;
import ${package}.dto.CustomerFindByCriteriaQuery;
import ${package}.dto.clientobject.CustomerType;
import com.alibaba.sofa.context.TenantContext;
import com.alibaba.sofa.dto.MultiResponse;
import ${package}.api.CustomerServiceI;
import ${package}.dto.CustomerAddCmd;
import ${package}.dto.clientobject.CustomerCO;
import com.alibaba.sofa.dto.Response;
import com.alibaba.sofa.exception.BasicErrorCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by fulan.zjf on 2017/11/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
public class CustomerServiceTest {

    @Autowired
    private CustomerServiceI customerService;

    @Test
    public void testCustomerAddSuccess( ) {
        //1.Prepare
        TenantContext.set("10001", BizCode.DD);
        CustomerAddCmd cmd = new CustomerAddCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCustomerName("alibaba");
        customerCO.setCustomerType(CustomerType.VIP);
        cmd.setCustomer(customerCO);

        //2.Execute
        Response response = customerService.addCustomer(cmd);

        //3.Expect
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testCustomerAddValidationFail( ) {
        //1.Prepare
        TenantContext.set("10001", BizCode.CGS);
        CustomerAddCmd cmd = new CustomerAddCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCustomerName("alibaba");
        customerCO.setCustomerType(CustomerType.VIP);
        cmd.setCustomer(customerCO);

        //2.Execute
        Response response = customerService.addCustomer(cmd);

        //3.Expect
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), BasicErrorCode.BIZ_ERROR.getErrCode());
    }

    @Test
    public void testCustomerAddRuleVoilation( ){
        //1.Prepare
        TenantContext.set("10001", BizCode.DD);
        CustomerAddCmd cmd = new CustomerAddCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCustomerName("alibaba");
        customerCO.setCustomerType(CustomerType.VIP);
        customerCO.setSource(AppConstants.SOURCE_AD);
        cmd.setCustomer(customerCO);

        //2.Execute
        Response response = customerService.addCustomer(cmd);

        //3.Expect
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), BasicErrorCode.BIZ_ERROR.getErrCode());
    }
}
