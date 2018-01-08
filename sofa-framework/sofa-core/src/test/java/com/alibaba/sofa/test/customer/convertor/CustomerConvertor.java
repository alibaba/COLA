package com.alibaba.sofa.test.customer.convertor;

import com.alibaba.sofa.common.ApplicationContextHelper;
import com.alibaba.sofa.convertor.ConvertorI;
import com.alibaba.sofa.test.customer.CustomerCO;
import com.alibaba.sofa.test.customer.CustomerDO;
import com.alibaba.sofa.test.customer.entity.CustomerEntity;
import com.alibaba.sofa.test.customer.entity.rule.CustomerRuleExtPt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * CustomerConvertor
 *
 * @author Frank Zhang
 * @date 2018-01-07 3:08 AM
 */
@Component
public class CustomerConvertor implements ConvertorI<CustomerCO, CustomerEntity, CustomerDO>{

    @Override
    public CustomerEntity clientToEntity(CustomerCO customerCO){
        CustomerEntity customerEntity = (CustomerEntity)ApplicationContextHelper.getBean(CustomerEntity.class);
        customerEntity.setCompanyName(customerCO.getCompanyName());
        customerEntity.setCustomerType(customerCO.getCustomerType());
        return customerEntity;
    }
}
