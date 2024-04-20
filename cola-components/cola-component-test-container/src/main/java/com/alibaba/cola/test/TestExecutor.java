package com.alibaba.cola.test;

import com.alibaba.cola.test.command.TestClassRunCmd;
import com.alibaba.cola.test.command.TestMethodRunCmd;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

/**
 * TestExecutor
 *
 * @author Frank Zhang
 * @date 2020-11-17 3:42 PM
 */
public class TestExecutor {


    private Launcher launcher;

    public TestExecutor(Launcher launcher) {
        this.launcher = launcher;
    }

    public void execute(TestClassRunCmd cmd) throws Exception {
        Class<?> testClz = Class.forName(cmd.getClassName());
        runClassTest(cmd, testClz);
    }

    public void execute(TestMethodRunCmd cmd) throws Exception {
        Class<?> testClz = Class.forName(cmd.getClassName());
        runMethodTest(cmd, testClz, cmd.getMethodName());
    }

    private void runMethodTest(TestMethodRunCmd cmd, Class<?> testClz, String methodName) throws Exception {
        // 创建测试方法
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectMethod(testClz, methodName))
                .build();

        // 运行测试方法
        launcher.execute(request, new MyTestExecutionListener());
    }


    private void runClassTest(TestClassRunCmd cmd, Class<?> testClz) {
        // 创建测试类
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectClass(testClz))
                .build();

        // 运行测试方法
        launcher.execute(request, new MyTestExecutionListener());
    }

    static class MyTestExecutionListener implements TestExecutionListener {
        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
                // 处理测试失败的情况，例如记录日志或发送通知
                System.err.println("Test failed: " + testIdentifier.getDisplayName());
                testExecutionResult.getThrowable().get().printStackTrace();
            }
        }
    }
}
