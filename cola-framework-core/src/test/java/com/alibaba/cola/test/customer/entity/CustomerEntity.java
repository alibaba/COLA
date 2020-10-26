package com.alibaba.cola.test.customer.entity;

import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.domain.EntityObject;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.test.customer.CustomerType;
import com.alibaba.cola.test.customer.entity.rule.CustomerRuleExtPt;
import com.alibaba.cola.test.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Customer Entity
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:38 AM
 */
@Entity
public class CustomerEntity extends EntityObject {

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
        extensionExecutor.execute(CustomerRuleExtPt.class,this.getBizScenario(), extension -> extension.addCustomerCheck(this));

        //Persist customer
        customerRepository.persist(this);

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
