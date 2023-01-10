package com.alibaba.cola.extension.test;

import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.alibaba.cola.extension.test.customer.app.extensionpoint.StatusNameConvertorExtPt;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jakarta.annotation.Resource;

/**
 * 多坐标测试
 *
 * @author wangguoqiang wrote on 2022/10/10 14:54
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MultiCoordinateTests {


    @Resource
    private ExtensionExecutor extensionExecutor;


    @Test
    public void testMultiCoordinate() {
        BizScenario bizScenario1 = BizScenario.valueOf("Samsung", "order", "scenario1");
        BizScenario bizScenario2 = BizScenario.valueOf("Samsung", "order", "scenario2");
        BizScenario bizScenario3 = BizScenario.valueOf("Samsung", "parts", "scenario1");
        BizScenario bizScenario4 = BizScenario.valueOf("Samsung", "parts", "scenario2");
        BizScenario bizScenario5 = BizScenario.valueOf("Motorola", "order", "scenario1");
        BizScenario bizScenario6 = BizScenario.valueOf("Motorola", "order", "scenario2");
        BizScenario bizScenario7 = BizScenario.valueOf("Motorola", "parts", "scenario1");
        BizScenario bizScenario8 = BizScenario.valueOf("Motorola", "parts", "scenario2");
        String name1 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario1, pt -> pt.statusNameConvertor(1));
        String name2 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario2, pt -> pt.statusNameConvertor(2));
        String name3 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario3, pt -> pt.statusNameConvertor(3));
        String name4 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario4, pt -> pt.statusNameConvertor(4));
        String name5 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario5, pt -> pt.statusNameConvertor(5));
        String name6 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario6, pt -> pt.statusNameConvertor(6));
        String name7 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario7, pt -> pt.statusNameConvertor(7));
        String name8 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario8, pt -> pt.statusNameConvertor(8));

        Assert.assertEquals("one",name1);
        Assert.assertEquals("two",name2);
        Assert.assertEquals("three",name3);
        Assert.assertEquals("four",name4);
        Assert.assertEquals("five",name5);
        Assert.assertEquals("six",name6);
        Assert.assertEquals("seven",name7);
        Assert.assertEquals("eight",name8);
    }

    @Test
    public void testMultiCoordinateWithAnnotation() {
        BizScenario bizScenario1 = BizScenario.valueOf("Sony", "user", "scenario3");
        BizScenario bizScenario2 = BizScenario.valueOf("Siemens", "order", "scenario1");
        String name1 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario1, pt -> pt.statusNameConvertor(1));
        String name2 = extensionExecutor.execute(StatusNameConvertorExtPt.class, bizScenario2, pt -> pt.statusNameConvertor(2));
        Assert.assertEquals("one",name1);
        Assert.assertEquals("two",name2);
    }
}
