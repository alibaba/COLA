package com.alibaba.craftsman.app;

import com.alibaba.cola.exception.BizException;
import com.alibaba.craftsman.dto.ATAMetricAddCmd;
import com.alibaba.craftsman.dto.clientobject.ATAMetricCO;
import com.alibaba.craftsman.interceptor.ValidationInterceptor;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static junit.framework.TestCase.fail;

/**
 * ValidationInterceptorTest
 *
 * @author Frank Zhang
 * @date 2019-03-02 3:04 PM
 */
public class ValidationInterceptorTest {

    private ValidationInterceptor validationInterceptor = new ValidationInterceptor();

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    private static class TypeTester{
        public Long boxedField;
        public long primitiveField;
        public String strField;
    }

    @Test
    public void testPrimitive() throws NoSuchFieldException {
        Field boxedField = TypeTester.class.getField("boxedField");
        Field primitiveField = TypeTester.class.getField("primitiveField");
        Field strField = TypeTester.class.getField("strField");

        Assert.assertTrue(ClassUtils.isPrimitiveOrWrapper(boxedField.getType()));
        Assert.assertTrue(ClassUtils.isPrimitiveOrWrapper(primitiveField.getType()));
        Assert.assertFalse(ClassUtils.isPrimitiveOrWrapper(TypeTester.class));
        Assert.assertFalse(ClassUtils.isPrimitiveOrWrapper(strField.getType()));
        Assert.assertTrue(strField.getType().equals(String.class));

    }

    @Test
    public void testCommandViolation(){
        ATAMetricAddCmd ataMetricAddCmd = new ATAMetricAddCmd();

        thrown.expect(BizException.class);
        thrown.expectMessage("ataMetricCO");

        validationInterceptor.preIntercept(ataMetricAddCmd);
    }

    @Test
    public void testClientObjectViolation(){
        ATAMetricAddCmd ataMetricAddCmd = new ATAMetricAddCmd();
        ATAMetricCO ataMetricCO = new ATAMetricCO();
        ataMetricCO.setTitle("testATAMetricAddSuccess");
        ataMetricCO.setUrl("sharingLink");
        ataMetricAddCmd.setAtaMetricCO(ataMetricCO);

        thrown.expect(BizException.class);
        thrown.expectMessage("ownerId");

        validationInterceptor.preIntercept(ataMetricAddCmd);
    }

    @Test
    public void testSuccessfullyValidation(){
        ATAMetricAddCmd ataMetricAddCmd = new ATAMetricAddCmd();
        ATAMetricCO ataMetricCO = new ATAMetricCO();
        ataMetricCO.setOwnerId("99888");
        ataMetricCO.setTitle("testATAMetricAddSuccess");
        ataMetricCO.setUrl("sharingLink");
        ataMetricAddCmd.setAtaMetricCO(ataMetricCO);

        validationInterceptor.preIntercept(ataMetricAddCmd);
    }

}
