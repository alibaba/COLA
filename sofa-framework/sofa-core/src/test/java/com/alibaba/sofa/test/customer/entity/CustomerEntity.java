package com.alibaba.sofa.test.customer.entity;

import com.alibaba.sofa.domain.Entity;
import com.alibaba.sofa.extension.ExtensionExecutor;
import com.alibaba.sofa.test.customer.CustomerType;
import com.alibaba.sofa.test.customer.convertor.CustomerConvertorExtPt;
import com.alibaba.sofa.test.customer.entity.rule.CustomerRuleExtPt;
import com.alibaba.sofa.test.customer.repository.CustomerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Customer Entity
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:38 AM
 */
@Data
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerEntity extends Entity {

    private String companyName;
    private SourceType sourceType;
    private CustomerType customerType;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ExtensionExecutor extensionExecutor;

    public CustomerEntity() {

    }

    public void addNewCustomer() {
        //Add customer policy
        extensionExecutor.execute(CustomerRuleExtPt.class, extension -> extension.addCustomerCheck(this));

        //Persist customer
        customerRepository.persist(this);
    }
}
