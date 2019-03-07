package com.alibaba.cola.test.extension;

import com.alibaba.cola.TestConfig;
import com.alibaba.cola.context.Context;
import com.alibaba.cola.exception.ColaException;
import com.alibaba.cola.extension.ExtensionExecutor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * LocateExtensionTest
 *
 * @author Frank Zhang
 * @date 2019-01-02 9:33 PM
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class LocateExtensionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ExtensionExecutor extensionExecutor;

    @Test
    public void testFullMatch() {
        Context context = new Context();
        context.setBizCode("ali.animal.dog");
        String dog = extensionExecutor.execute(AnimalExtPt.class, context, extension -> extension.eat());
        Assert.assertEquals(dog, "Dog");
    }

    @Test
    public void testPartMatch() {
        Context context = new Context();
        context.setBizCode("ali.animal");
        String dog = extensionExecutor.execute(AnimalExtPt.class, context, extension -> extension.eat());
        Assert.assertEquals(dog, "CommonAnimal");
    }

    @Test
    public void testDefaultWithNullExistBizCode() {
        Context context = new Context();
        context.setBizCode("ali.noexist");
        String dog = extensionExecutor.execute(AnimalExtPt.class, context, extension -> extension.eat());
        Assert.assertEquals(dog, "DefaultAnimal");
    }

    @Test
    public void testDefaultWithNoBizCode() {
        Context context = new Context();
        String dog = extensionExecutor.execute(AnimalExtPt.class, context, extension -> extension.eat());
        Assert.assertEquals(dog, "DefaultAnimal");
    }

    @Test(expected= ColaException.class)
    public void testContextNull() throws ColaException{
        extensionExecutor.execute(AnimalExtPt.class, null, extension -> extension.eat());
        thrown.expect(ColaException.class);
    }
}
