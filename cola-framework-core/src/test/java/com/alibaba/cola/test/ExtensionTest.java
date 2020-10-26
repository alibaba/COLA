package com.alibaba.cola.test;

import com.alibaba.cola.TestSpringConfig;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.test.customer.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ExtensionTest
 *
 * @author Frank Zhang
 * @date 2020-08-20 12:45 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestSpringConfig.class})
public class ExtensionTest {

    @Resource
    private CustomerServiceI customerService;

    @Test
    public void testBiz1UseCase1Scenario1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_RFQ);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1, Constants.USE_CASE_1, Constants.SCENARIO_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBiz1UseCase1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("alibaba");
        customerCO.setSource(Constants.SOURCE_RFQ);
        customerCO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerCO(customerCO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1, Constants.USE_CASE_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testBiz1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerCO customerCO = new CustomerCO();
        customerCO.setCompanyName("jingdong");
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
}
