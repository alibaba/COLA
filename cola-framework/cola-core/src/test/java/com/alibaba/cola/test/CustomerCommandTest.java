package com.alibaba.cola.test;

import com.alibaba.cola.TestSpringConfig;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.extension.BizScenario;
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
@ContextConfiguration(classes = {TestSpringConfig.class})
public class CustomerCommandTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private CustomerServiceI customerService;

    @Value("${bizScenarioUniqueIdentity}")
    private String bizCode;

    @Before
    public void setUp() {
    }

    @Test
    public void testBizOneAddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_RFQ);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

    @Test(expected = BizException.class)
    public void testBizOneAddCustomerFailure(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_AD);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_2);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect exception

    }

    @Test
    public void testBizTwoAddCustomer(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_AD);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_2);
        addCustomerCmd.setBizScenario(scenario);
        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

 /*   @Test
    public void testCompanyTypeViolation(){
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(SourceType.AD.name());
        customerCO.setCustomerType(CustomerType.VIP);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1);
        addCustomerCmd.setBizScenario(scenario);
        Response response = customerService.addCustomer(addCustomerCmd);

        //Expect biz exception
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), BasicErrorCode.BIZ_ERROR.getErrCode());
    }
 */


/*    @Test
    public void testParamValidationFail(){
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1);
        addCustomerCmd.setBizScenario(scenario);
        Response response = customerService.addCustomer(addCustomerCmd);

        //Expect parameter validation error
        Assert.assertFalse(response.isSuccess());
        Assert.assertEquals(response.getErrCode(), BasicErrorCode.BIZ_ERROR.getErrCode());
    }*/
}
