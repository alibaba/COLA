package com.alibaba.cola.domain.annotationentity;

import com.alibaba.cola.domain.Application;
import com.alibaba.cola.domain.Customer;
import com.alibaba.cola.domain.DomainFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liuziyuan
 * @date 12/4/2020 2:53 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class UserTest {

    @Test
    public void testFactoryToCreateAggregateUser(){
        User user = DomainFactory.aggregateCreate(User.class);
        Assert.assertTrue(user.getAddress() != null);
        Assert.assertTrue(user.getAddress().getZipCode() != null);
    }
}
