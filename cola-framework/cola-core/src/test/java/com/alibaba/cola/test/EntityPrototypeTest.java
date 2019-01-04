package com.alibaba.cola.test;

import com.alibaba.cola.TestConfig;
import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.test.customer.entity.CustomerE;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * EntityPrototypeTest
 *
 * @author Frank Zhang
 * @date 2018-01-07 12:34 PM
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class EntityPrototypeTest {

    @Test
    public void testPrototype(){
        CustomerE customerEntity1 = DomainFactory.create(CustomerE.class);
        System.out.println(customerEntity1);
        CustomerE customerEntity2 = DomainFactory.create(CustomerE.class);
        System.out.println(customerEntity2);

        Assert.assertEquals(customerEntity1, customerEntity2);
        Assert.assertFalse(customerEntity1 == customerEntity2); //It should be different objects
    }
}
