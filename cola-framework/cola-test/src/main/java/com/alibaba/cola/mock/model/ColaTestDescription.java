package com.alibaba.cola.mock.model;

import java.lang.reflect.Method;

import com.alibaba.cola.container.command.AbstractCommand;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;

/**
 * @author shawnzhan.zxy
 * @date 2019/03/25
 */
public class ColaTestDescription {
    Description description;
    Object testInstance;
    AbstractCommand command;

    public static ColaTestDescription createTestDescription(AbstractCommand command, Object testInstance, Method method){
        return new ColaTestDescription(command, testInstance, method);
    }

    public static ColaTestDescription createTestDescription(AbstractCommand command, Object testInstance){
        return new ColaTestDescription(command, testInstance, null);
    }

    public ColaTestDescription(){}
    public ColaTestDescription(Description description){
        this.description = description;
    }
    public ColaTestDescription(Object testInstance, Description description){
        this.description = description;
        this.testInstance = testInstance;
    }
    public ColaTestDescription(AbstractCommand command, Object testInstance, Method method){
        this.command = command;
        this.testInstance = testInstance;

        if(method != null){
            this.description = Description.createTestDescription(testInstance.getClass(), method.getName(), method.getAnnotations());
        }else{
            this.description = Description.createSuiteDescription(testInstance.getClass());
        }
    }

    public Description getDescription() {
        return description;
    }

    public Object getTestInstance() {
        return testInstance;
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setTestInstance(Object testInstance) {
        this.testInstance = testInstance;
    }

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }
}
