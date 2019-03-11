package com.alibaba.cola.container;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.cola.mock.ColaTestRecordController;
import com.alibaba.cola.mock.autotest.ColaTestGenerator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.cola.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.cola.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 *
 * @author fulan.zjf
 *
 */
/**
 * 这是一个轻量级的TDD测试工具，可以敏捷的在开发过程中，运行测试，功能如下：
 * 1.测试单个方法，请在控制台输入方法全称
 *   例如：com.alibaba.cola.sales.service.test.CustomerServiceTest.testCheckConflict()
 * 2.测试整个测试类，请在控制台输入类全称
 *   例如：com.alibaba.cola.sales.service.test.CustomerServiceTest
 * 3.重复上一次测试，只需在控制台输入字母 - ‘r’
 * 
 * @author fulan.zjf
 *
 */
@Component
public class TestsContainer implements ApplicationContextAware{

    private static final String WELCOME_INPUT = "$Welcome$";
    
    private static final String REPEAT_INPUT = "r";
    /** 创建测试类命令*/
    private static final String CREATE_TEST_INPUT = "new";
    private static final String SPACE = " ";

    private static ApplicationContext context;
    
    private static TestExecutor testExecutor;
    private static AtomicBoolean initFlag = new AtomicBoolean(false);
    
    private static boolean isInputValid = true;

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
            System.out.println("**** 例如：com.alibaba.cola.sales.service.test.CustomerServiceTest.testCheckConflict()");
            System.out.println("**** 2.测试整个测试类，请在控制台输入类全称");
            System.out.println("**** 例如：com.alibaba.cola.sales.service.test.CustomerServiceTest");
            System.out.println("**** 3.重复上一次测试，只需在控制台输入字母 - ‘r’");
            System.out.println("**** 4.自动生成ColaTest测试类,请输入‘new 方法全称  参数1 参数2 ...’");
            System.out.println("**** 例如：new com.alibaba.crm.sales.domain.customer.entity.CustomerE#addContact");
            System.out.println("***********************************************************************************");
            return;
        }
        if (input.indexOf(".") == -1){
            isInputValid = false;
            System.err.println("Your input is not a valid qualified name");
            return;         
        }
        if(input.startsWith(CREATE_TEST_INPUT + SPACE)){
            createTestClassCmd(input);
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

    private static void createTestClassCmd(String cmd){
        cmd = cmd.replaceAll(" +", " ");
        String[] cmdParams = cmd.split(" ");
        if(cmdParams.length < 2){
            System.err.println("Your input is not a valid");
            return;
        }
        ColaTestGenerator generator = new ColaTestGenerator(cmdParams[1], ColaTestRecordController.getTemplateSuperClassName());
        generator.generate(Arrays.copyOfRange(cmdParams, 2, cmdParams.length));
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

    public static TestExecutor getTestExecutor() {
        return testExecutor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
