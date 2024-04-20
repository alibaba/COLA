package com.alibaba.cola.test;

import com.alibaba.cola.test.command.AbstractCommand;
import com.alibaba.cola.test.command.GuideCmd;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TestsContainer
 *
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 *
 * @author Frank Zhang
 * @date 2020-11-17 3:35 PM
 */
public class TestsContainer {

    private static ApplicationContext context;
    private static Launcher launcher;

    private static TestExecutor testExecutor;
    private static AtomicBoolean initFlag = new AtomicBoolean(false);


    /**
     * 如果要用到Junit5的Extension功能，需要显示的提供Launcher
     *
     * @param context  ApplicationContext to be provided
     * @param launcher 运行Junit5测试用例的Launcher, 如果不提供，默认会自己创建一个
     */
    public static void start(ApplicationContext context, Launcher launcher) {
        TestsContainer.context = context;
        if (launcher != null) {
            TestsContainer.launcher = launcher;
        } else {
            TestsContainer.launcher = LauncherFactory.create();
        }
        testExecutor = new TestExecutor(TestsContainer.launcher);
        monitorConsole();
    }

    /**
     * 使用Junit5的launcher之后，不再需要ApplicationContext，框架会自己处理Spring的依赖关系
     * @param launcher
     */
    public static void start(Launcher launcher) {
        start(null, launcher);
    }

    /**
     * TestsContainer is optional to be in Spring Container
     *
     * @param context ApplicationContext to be provided
     */
    public static void start(ApplicationContext context) {
        start(context, null);
    }

    /**
     * TestsContainer without Spring Container
     */
    public static void start() {
        start(null, null);
    }

    public static void execute(String input) {
        if (ObjectUtils.isEmpty(input)) {
            return;
        }
        input = input.trim();
        AbstractCommand command = AbstractCommand.createCmd(input);
        if (command == null) {
            System.err.println("Your input is not a valid qualified name");
            return;
        }

        command.execute();
    }

    public static TestExecutor getTestExecutor() {
        return testExecutor;
    }

    private static void monitorConsole() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
                System.in));
        String input = GuideCmd.GUIDE_HELP;
        while (true) {
            try {
                execute(input);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
                break;
            }
            try {
                input = bufferRead.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
