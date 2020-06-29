package com.alibaba.cola.container;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.cola.container.command.AbstractCommand;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static com.alibaba.cola.mock.utils.Constants.GUIDE_HELP;

/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 *
 * @author fulan.zjf
 *
 */
/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.framework.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 * 
 * @author fulan.zjf
 *
 */
@Component
public class TestsContainer implements ApplicationContextAware{

    private static ApplicationContext context;
    
    private static TestExecutor testExecutor;
    private static AtomicBoolean initFlag = new AtomicBoolean(false);
    
    public static void init(ApplicationContext context){
        if(!initFlag.compareAndSet(false, true)) {
            return;
        }
        if(context == null){
            testExecutor = new TestExecutor(TestsContainer.context);
        }else {
            testExecutor = new TestExecutor(context);
        }
    }

    /**
     * TestsContainer is optional to be in Spring Container
     * @param context ApplicationContext to be provided
     */
    public static void start(ApplicationContext context) {
        TestsContainer.context = context;
        start();
    }

    /**
     * TestsContainer must be within Spring Container
     */
    public static void start(){
        init(TestsContainer.context);
        monitorConsole();
    }

    public static void execute(String input){
        if(StringUtils.isBlank(input)){
            return;
        }
        input = input.trim();
        AbstractCommand command = AbstractCommand.createCmd(input);
        if (command == null){
            System.err.println("Your input is not a valid qualified name");
            return;         
        }

        command.execute();
    }

    public static TestExecutor getTestExecutor() {
        return testExecutor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private static void monitorConsole(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
            System.in));
        String input = GUIDE_HELP;
        while (true) {
            try {
                execute(input);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e){
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
