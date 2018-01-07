package com.alibaba.sofa.pandora.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 *
 * @author fulan.zjf
 *
 */
/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 * 
 * @author fulan.zjf
 *
 */
@Component
public class TestsContainer implements ApplicationContextAware{

    private static final String WELCOME_INPUT = "$Welcome$";
    
    private static final String REPEAT_INPUT = "r";
    
    private static ApplicationContext context;
    
    private static TestExecutor testExecutor;
    
    private static boolean isInputValid = true;
    
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
    public static void start( ){
        
        testExecutor = new TestExecutor(context);
        
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
                System.in));
        String input = WELCOME_INPUT;
        String lastInput = input;
        while (!(input).equalsIgnoreCase("quit")) {
            try {
                isInputValid = true;
                if (REPEAT_INPUT.equalsIgnoreCase(input)){
                    input = lastInput;
                }
                //execute
                execute(input);
                
            } catch (Throwable e) {
                e.printStackTrace();
            }
            finally{
                if (!REPEAT_INPUT.equalsIgnoreCase(input) && isInputValid){
                    lastInput = input;
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

    private static void execute(String input) throws Exception{
        if (input == null || input.trim().length()==0){
            isInputValid = false;
            System.err.println("Your input is empty, please input a valid qualified name");
            return;
        }
        if (WELCOME_INPUT.equals(input)){
            System.out.println("************** 欢迎使用轻量级TDD测试工具 ***************************");
            System.out.println("**** 1.测试单个方法，请在控制台输入方法全称");
            System.out.println("**** 例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest.testCheckConflict()");
            System.out.println("**** 2.测试整个测试类，请在控制台输入类全称");
            System.out.println("**** 例如：com.alibaba.sofa.sales.service.test.CustomerServiceTest");
            System.out.println("**** 3.重复上一次测试，只需在控制台输入字母 - ‘r’");
            System.out.println("***********************************************************************************");
            return;
        }
        if (input.indexOf(".") == -1){
            isInputValid = false;
            System.err.println("Your input is not a valid qualified name");
            return;         
        }
        boolean isMethod = false;
        if (isEclipseMethod(input) || isIdeaMethod(input)){
            isMethod = true;
        }
        System.out.println("===Run "+(isMethod?"Method":"Class")+" start==== "+input);
        if(isMethod){
            String className = null;
            String methodName = null;
            if (isEclipseMethod(input)) {
                methodName = input.substring(input.lastIndexOf(".")+1, input.indexOf("("));
                className = input.substring(0, input.lastIndexOf("."));
            }
            if (isIdeaMethod(input)) {
                methodName = input.substring(input.lastIndexOf("#")+1, input.length());
                className = input.substring(0, input.lastIndexOf("#"));
            }
            if(methodName == null || className == null) {
                System.err.println("Your input " + input + " is not valid");
                return;
            }
            testExecutor.setClassName(className);
            testExecutor.setMethodName(methodName);
            testExecutor.testMethod();
        }
        else {
            testExecutor.setClassName(input);
            testExecutor.testClass();
        }
        System.out.println("===Run "+(isMethod?"Method":"Class")+" end====\n");
    }

    /**
     * @param input
     * @return
     */
    private static boolean isEclipseMethod(String input) {
        return input.indexOf("(") > 0 ;
    }
    
    private static boolean isIdeaMethod(String input) {
        return input.indexOf("#") > 0 ;//to accommodate idea
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
