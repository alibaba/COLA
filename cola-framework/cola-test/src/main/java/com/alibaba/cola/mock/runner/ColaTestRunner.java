package com.alibaba.cola.mock.runner;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.cola.mock.listener.IntegrateTestListener;
import com.alibaba.cola.mock.model.ColaTestDescription;
import com.alibaba.cola.mock.spring.ColaContextLoader;
import com.alibaba.cola.mock.utils.reflection.BeanPropertySetter;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.ColaBefore;
import com.alibaba.cola.mock.listener.UnitTestListener;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.annotation.ProfileValueUtils;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks;
import org.springframework.test.context.support.DefaultTestContext;

/**
 * @author shawnzhan.zxy
 * @date 2018/09/02
 */
public class ColaTestRunner extends SpringJUnit4ClassRunner{
    public static AtomicBoolean init = new AtomicBoolean(false);

    /**
     * Construct a new {@code SpringJUnit4ClassRunner} and initialize a
     * {@link } to provide Spring testing functionality to
     * standard JUnit tests.
     *
     * @param clazz the test class to be run
     * @see #createTestContextManager(Class)
     */
    public ColaTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    protected Object createTest(Description description) throws Exception {
        assembleContextLoader();
        Object testInstance = super.createTest();
        ColaTestDescription colaTestDescription = new ColaTestDescription(testInstance, description);
        ColaMockito.g().initMock(colaTestDescription);
        return testInstance;
    }

    @Override
    protected Statement methodBlock(FrameworkMethod frameworkMethod) {
        Object testInstance;
        try {
            testInstance = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    Description description = describeChild(frameworkMethod);
                    return createTest(description);
                }
            }.run();
        }
        catch (Throwable ex) {
            return new Fail(ex);
        }

        Statement statement = methodInvoker(frameworkMethod, testInstance);
        statement = possiblyExpectingExceptions(frameworkMethod, testInstance, statement);
        statement = withBefores(frameworkMethod, testInstance, statement);
        if(!ColaMockito.g().getContext().isRecording()) {
            statement = withColaBefores(frameworkMethod, testInstance, statement);
            statement = new RunBeforeTestMethodCallbacks(statement, testInstance, frameworkMethod.getMethod(),
                getTestContextManager());
        }
        statement = withAfters(frameworkMethod, testInstance, statement);
        //statement = withRulesReflectively(frameworkMethod, testInstance, statement);
        statement = withPotentialRepeat(frameworkMethod, testInstance, statement);
        statement = withPotentialTimeout(frameworkMethod, testInstance, statement);
        return statement;
    }

    /**
     * Check whether the test is enabled in the current execution environment.
     * <p>This prevents classes with a non-matching {@code @IfProfileValue}
     * annotation from running altogether, even skipping the execution of
     * {@code prepareTestInstance()} methods in {@code TestExecutionListeners}.
     * @see ProfileValueUtils#isTestEnabledInThisEnvironment(Class)
     * @see org.springframework.test.annotation.IfProfileValue
     * @see org.springframework.test.context.TestExecutionListener
     */
    @Override
    public void run(RunNotifier notifier) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        addColaUnitTestListener(notifier);
        super.run(notifier);
    }

    protected Statement withColaBefores(FrameworkMethod method, Object target,
                                        Statement statement) {
        List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(
            ColaBefore.class);
        return befores.isEmpty() ? statement : new RunBefores(statement,
            befores, target);
    }

    private void addColaUnitTestListener(RunNotifier notifier){
        if(ColaTestUnitRunner.init.compareAndSet(false, true)){
            notifier.addListener(ColaTestUnitRunner.colaUnitTestListener);
        }
    }

    private void assembleContextLoader(){
        BeanPropertySetter beanProperty = new BeanPropertySetter(getTestContextManager().getTestContext(), "mergedContextConfiguration");
        MergedContextConfiguration mergedContextConfiguration = beanProperty.getValue();
        beanProperty = new BeanPropertySetter(mergedContextConfiguration, "contextLoader");
        beanProperty.setValue(new ColaContextLoader());
    }
}
