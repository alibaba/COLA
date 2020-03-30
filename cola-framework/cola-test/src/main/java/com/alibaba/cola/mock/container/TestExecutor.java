//package com.alibaba.cola.mock.container;
//
//import java.lang.reflect.Constructor;
//
//import org.junit.runner.Description;
//import org.junit.runner.JUnitCore;
//import org.junit.runner.RunWith;
//import org.junit.runner.Runner;
//import org.junit.runner.manipulation.Filter;
//import org.junit.runner.manipulation.Filterable;
//import org.junit.runner.notification.Failure;
//import org.junit.runner.notification.RunListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author shawnzhan.zxy
// * @date 2018/09/14
// */
//public class TestExecutor {
//    private static final Logger logger = LoggerFactory.getLogger(TestExecutor.class);
//    private String className;
//    private String methodName;
//
//    public void testClass() throws Exception {
//        Class<?> testClz = Class.forName(className);
//        runTest(testClz, null);
//    }
//
//    public void testMethod() throws Exception {
//        Class<?> testClz = Class.forName(className);
//        runTest(testClz, new Filter() {
//            @Override
//            public boolean shouldRun(Description description) {
//                if(methodName.equals(description.getMethodName())){
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public String describe() {
//                return methodName;
//            }
//        });
//    }
//
//    private void runTest(Class<?> testClz, Filter filter) throws Exception {
//        RunWith runWith = testClz.getAnnotation(RunWith.class);
//        Constructor constructor = runWith.value().getConstructor(Class.class);
//        Object runner = constructor.newInstance(testClz);
//        if(filter != null) {
//            ((Filterable)runner).filter(filter);
//        }
//        JUnitCore jUnitCore = new JUnitCore();
//        jUnitCore.addListener(new RunListener(){
//            @Override
//            public void testFailure(Failure failure) throws Exception {
//                logger.error(failure.getDescription().getDisplayName() + " error!", failure.getException());
//            }
//
//            @Override
//            public void testFinished(Description description) throws Exception {
//                logger.info(description.getClassName() + " " + description.getDisplayName() + " end.");
//            }
//
//            @Override
//            public void testStarted(Description description) throws Exception {
//                logger.info(description.getClassName() + " " + description.getDisplayName() + " start...");
//            }
//        });
//        jUnitCore.run((Runner)runner);
//    }
//
//    /**
//     * @param className the className to set
//     */
//    public void setClassName(String className) {
//        this.className = className;
//    }
//
//    /**
//     * @param methodName the methodName to set
//     */
//    public void setMethodName(String methodName) {
//        this.methodName = methodName;
//    }
//
//
//}