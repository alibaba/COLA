package com.alibaba.sofa.test.customer.repository;

import com.alibaba.sofa.repository.RepositoryI;
import com.alibaba.sofa.test.customer.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

/**
 * CustomerRepository
 *
 * @author Frank Zhang
 * @date 2018-01-07 11:59 AM
 */
@Repository
public class CustomerRepository implements RepositoryI {

    public void persist(CustomerEntity customerEntity){
        System.out.println("Persist customer to DB : "+ customerEntity);
    }
}
