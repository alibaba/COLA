package com.alibaba.cola.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liuziyuan
 * @date 12/4/2020 9:47 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CustomerTest {

    @Test
    public void testFactoryToCreateCustomerAndGatewayToSetValue(){
        Customer customer = DomainFactory.create(Customer.class);
        Assert.assertTrue(customer.getPurchasePowerScore().equals(96L));
    }
}
