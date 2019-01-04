package com.alibaba.cola.test.customer.entity;

import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.domain.EntityObject;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.test.customer.CustomerType;
import com.alibaba.cola.test.customer.entity.extensionpoint.CustomerCheckExtPt;
import com.alibaba.cola.test.customer.repository.CustomerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Customer EntityObject
 *
 * @author Frank Zhang
 * @date 2018-01-07 2:38 AM
 */
@Data
@Entity
public class CustomerE extends EntityObject {

    private String companyName;
    private SourceType sourceType;
    private CustomerType customerType;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ExtensionExecutor extensionExecutor;

    public CustomerE() {

    }

    public void addNewCustomer() {
        //Add customer policy
        extensionExecutor.executeVoid(CustomerCheckExtPt.class, this.getContext(), extension -> extension.addCustomerCheck(this));

        //Persist customer
        customerRepository.persist(this);
    }
}
