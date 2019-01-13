package com.alibaba.cola.test;

import com.alibaba.cola.TestConfig;
import com.alibaba.cola.context.Context;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BasicErrorCode;
import com.alibaba.cola.test.customer.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * CustomerCommandTest
 *
 * @author Frank Zhang 2018-01-06 7:55 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class CustomerCommandTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private CustomerServiceI customerService;

    @Value("${bizCode}")
    private String bizCode;

    private Context context;

    @Before
    public void setUp() {
        context = new Context();
    }

    @Test
    public void testBizOneAddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        context.setBizCode(Constants.BIZ_ONE);
        addCustomerCmd.setContext(context);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_RFQ);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBizOneAddCustomerFailure(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        context.setBizCode(Constants.BIZ_ONE);
        addCustomerCmd.setContext(context);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_AD);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect exception
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(),ErrorCode.B_CUSTOMER_advNotAllowed.getErrCode());
    }

    @Test
    public void testBizTwoAddCustomer(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        context.setBizCode(Constants.BIZ_TWO);
        addCustomerCmd.setContext(context);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_AD);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBizTwoAddCustomerFailure(){
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        addCustomerCmd.setContext(context);
        context.setBizCode(Constants.BIZ_TWO);
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource("p4p");
        customerCO.setCustomerType(CustomerType.VIP);
        addCustomerCmd.setCustomerCO(customerCO);
        Response response = customerService.addCustomer(addCustomerCmd);

        //Expect biz exception
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), ErrorCode.B_CUSTOMER_vipNeedApproval.getErrCode());
    }

    @Test
    public void testParamValidationFail(){
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        addCustomerCmd.setContext(context);
        Response response = customerService.addCustomer(addCustomerCmd);

        //Expect parameter validation error
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), BasicErrorCode.B_COMMON_ERROR.getErrCode());
    }
}
