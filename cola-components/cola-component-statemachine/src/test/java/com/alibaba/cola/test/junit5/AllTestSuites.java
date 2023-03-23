package com.alibaba.cola.test.junit5;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(org.junit.platform.runner.JUnitPlatform.class)
@SuiteDisplayName("suite launcher")
@SelectPackages("com.alibaba.cola.test.junit5")
public class AllTestSuites {
}
