package com.alibaba.cola.mock.listener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.cola.mock.model.ColaTestDescription;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/23
 */
public class ColaNotifier {
    private static final Logger logger = LoggerFactory.getLogger(ColaNotifier.class);
    private final List<RunListener> listeners = new CopyOnWriteArrayList<RunListener>();
    private ColaRunListener colaRunListener;

    public void addListener(RunListener listener) {
        if (listener == null) {
            throw new NullPointerException("Cannot add a null listener");
        }
        listeners.add(listener);
    }

    /**
     * runner开始
     * @param colaDes
     */
    public void fireTestRunStarted(final ColaTestDescription colaDes){
        if(colaRunListener != null){
            colaRunListener.testRunStarted(colaDes);
        }

        for (RunListener listener : listeners) {
            try {
                listener.testRunStarted(colaDes.getDescription());
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * 方法级开始
     * @param testMethod
     * @param description
     */
    public void fireTestStarted(final Method testMethod, final Description description){
        Description methodDescription = getMethodDescription(testMethod, description);
        for (RunListener listener : listeners) {
            try {
                listener.testStarted(methodDescription);
            } catch (Exception e) {
                logger.error("", e);
            }
        }

    }

    /**
     * 方法级结束
     * @param testMethod
     * @param description
     */
    public void fireTestFinished(Method testMethod, Description description){
        Description methodDescription = getMethodDescription(testMethod, description);
        for (RunListener listener : listeners) {
            try {
                listener.testFinished(methodDescription);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * runner结束( 多个test共用)
     * @param description
     */
    public void fireTestRunFinished(final Description description){
        for (RunListener listener : listeners) {
            try {
                listener.testRunFinished(new Result());
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private Description getMethodDescription(Method testMethod, Description description){
        if(description.getChildren() != null && description.getChildren().size() > 0){
            description = description.getChildren().stream().filter(p->{
                if(testMethod.getName().equals(p.getMethodName())){
                    return true;
                }
                return false;
            }).findFirst().get();
        }
        return description;
    }

    public void setColaRunListener(ColaRunListener colaRunListener) {
        this.colaRunListener = colaRunListener;
    }
}
