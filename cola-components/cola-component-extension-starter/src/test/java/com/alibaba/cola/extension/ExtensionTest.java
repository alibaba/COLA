package com.alibaba.cola.extension;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.extension.customer.client.AddCustomerCmd;
import com.alibaba.cola.extension.customer.client.Constants;
import com.alibaba.cola.extension.customer.client.CustomerDTO;
import com.alibaba.cola.extension.customer.client.CustomerServiceI;
import com.alibaba.cola.extension.customer.domain.CustomerType;
import jakarta.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



/**
 * ExtensionTest
 *
 * @author Frank Zhang
 * @date 2020-11-14 2:55 PM
 */
@SpringBootTest(classes = Application.class)
public class ExtensionTest {
    @Resource
    private CustomerServiceI customerService;

    @Test
    public void testBiz1UseCase1Scenario1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCompanyName("alibaba");
        customerDTO.setSource(Constants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1, Constants.USE_CASE_1, Constants.SCENARIO_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testBiz1UseCase1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCompanyName("alibaba");
        customerDTO.setSource(Constants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1, Constants.USE_CASE_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testBiz1AddCustomerSuccess(){
        //1. Prepare
        AddCustomerCmd addCustomerCmd = new AddCustomerCmd();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCompanyName("jingdong");
        customerDTO.setSource(Constants.SOURCE_RFQ);
        customerDTO.setCustomerType(CustomerType.IMPORTANT);
        addCustomerCmd.setCustomerDTO(customerDTO);
        BizScenario scenario = BizScenario.valueOf(Constants.BIZ_1);
        addCustomerCmd.setBizScenario(scenario);

        //2. Execute
        Response response = customerService.addCustomer(addCustomerCmd);

        //3. Expect Success
        Assertions.assertTrue(response.isSuccess());
    }
}
