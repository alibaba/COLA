package com.alibaba.cola.mock.runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.model.MockServiceModel;

import org.mockito.InjectMocks;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.withSettings;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/04
 */
public class IntegrateColaTest extends AbstractColaTest {

    public IntegrateColaTest(ColaMockito colaMockito) {
        super(colaMockito);
    }

    @Override
    protected void initTest(Object testInstance) {
        ColaMockito.g().getContext().setTestInstance(testInstance);
        process(testInstance.getClass(), testInstance);
    }

    public List<Field> process(Class<?> context, Object testInstance) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                field.setAccessible(true);
                Object instance;
                Object mock = null;
                try {
                    instance = field.get(testInstance);
                    assertNotInterface(instance, field.getType());
                    if (new MockUtil().isMock(instance)) {
                        Mockito.reset(instance);
                        mock = instance;
                    } else if (instance != null) {
                        mock = Mockito.mock(instance.getClass(), withSettings()
                            .spiedInstance(instance)
                            .defaultAnswer(Mockito.CALLS_REAL_METHODS)
                            .name(field.getName()));
                        field.set(testInstance, mock);
                    } else {
                        mock = newSpyInstance(testInstance, field);
                        field.set(testInstance, mock);
                    }
                    putSpyMock(field, mock, instance);
                    fieldList.add(field);
                } catch (Exception e) {
                    throw new MockitoException("Unable to initialize @Spy annotated field '" + field.getName() + "'.\n" + e.getMessage(), e);
                }
            }
        }
        return fieldList;
    }

    private void assertNotInterface(Object testInstance, Class<?> type) {
        type = testInstance != null? testInstance.getClass() : type;
        if (type.isInterface()) {
            throw new MockitoException("Type '" + type.getSimpleName() + "' is an interface and it cannot be spied on.");
        }
    }

    private Object newSpyInstance(Object testInstance, Field field)
        throws InstantiationException, IllegalAccessException, InvocationTargetException {
        MockSettings settings = withSettings()
            .defaultAnswer(Mockito.CALLS_REAL_METHODS)
            .name(field.getName());
        Class<?> type = field.getType();
        if (type.isInterface()) {
            return Mockito.mock(type, settings.useConstructor());
        }
        if (!Modifier.isStatic(type.getModifiers())) {
            Class<?> enclosing = type.getEnclosingClass();
            if (enclosing != null) {
                if (!enclosing.isInstance(testInstance)) {
                    throw new MockitoException("@Spy annotation can only initialize inner classes declared in the test. "
                        + "Inner class: '" + type.getSimpleName() + "', "
                        + "outer class: '" + enclosing.getSimpleName() + "'.");
                }
                return Mockito.mock(type, settings
                    .useConstructor()
                    .outerInstance(testInstance));
            }
        }
        Constructor<?> constructor;
        try {
            constructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new MockitoException("Please ensure that the type '" + type.getSimpleName() + "' has 0-arg constructor.");
        }

        if (Modifier.isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
            return Mockito.mock(type, settings
                .spiedInstance(constructor.newInstance()));
        } else {
            return Mockito.mock(type, settings.useConstructor());
        }
    }

    private void putSpyMock(Field field, Object mock, Object oriTarget){
        MockServiceModel mockServiceModel = new MockServiceModel(field.getType(), field.getName(), oriTarget, mock);
        ColaMockito.g().getContext().putSpy(mockServiceModel);
    }
}
