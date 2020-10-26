package com.alibaba.cola.test.customer.convertor;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.test.customer.AddCustomerCmd;
import com.alibaba.cola.test.customer.CustomerCO;
import com.alibaba.cola.test.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * CustomerConvertor
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Component
public class CustomerConvertor{

    public CustomerEntity clientToEntity(Object clientObject){
        AddCustomerCmd addCustomerCmd = (AddCustomerCmd)clientObject;
        CustomerCO customerCO=addCustomerCmd.getCustomerCO();
        CustomerEntity customerEntity = (CustomerEntity)ApplicationContextHelper.getBean(CustomerEntity.class);
        customerEntity.setCompanyName(customerCO.getCompanyName());
        customerEntity.setCustomerType(customerCO.getCustomerType());
        customerEntity.setBizScenario(addCustomerCmd.getBizScenario());
        return customerEntity;
    }
}
