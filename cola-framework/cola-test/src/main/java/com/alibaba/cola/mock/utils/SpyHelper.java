package com.alibaba.cola.mock.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.cola.mock.ColaMockito;
import com.alibaba.cola.mock.annotation.Inject;
import com.alibaba.cola.mock.annotation.InjectOnlyTest;
import com.alibaba.cola.mock.scan.InjectAnnotationScanner;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/05
 */
public class SpyHelper {
    Object owner;
    Class ownerClazz;
    Set<Object> mocks = new HashSet<>();
    //InjectingAnnotationEngine injectingAnnotationEngine = new InjectingAnnotationEngine();
    MockUtil mockUtil;
    private final AnnotationEngine delegate = new DefaultAnnotationEngine();
    private final AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    public SpyHelper(Class ownerClazz, Object owner){
        this.owner = owner;
        this.ownerClazz = ownerClazz;
        //throwExceptionIfHasAnnotationMock();
        initMockUtil();
    }

    /**
     * 兼容mockito-core:2.23.4
     * @return
     */
    private void initMockUtil() {
        try {
            mockUtil = new MockUtil();
        }
        catch (IllegalAccessError error){
            try {
                Constructor constructor = MockUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                mockUtil = (MockUtil)constructor.newInstance();
            } catch (Exception e) {
                throw  new RuntimeException(e.getMessage());
            }
        }

    }

    /**
     * 单元测 录制
     * 集成测 录制
     */
    public void processInject4Record(){
        scanAndCreateMockitoFields();
        Set<Field> mockDependentFields = new HashSet<Field>();
        new InjectAnnotationScanner(ownerClazz, Inject.class).addTo(mockDependentFields);
        inject(mockDependentFields, mocks);
    }


    /**
     * 单元测 回放(不用清理)
     * 集成测 回放(要清理)
     * @param extendMocks
     */
    public void processInject4Test(Set<Object> extendMocks){
        scanAndCreateMockitoFields();
        Set<Field> mockDependentFields = new HashSet<Field>();
        new InjectAnnotationScanner(ownerClazz, InjectOnlyTest.class).addTo(mockDependentFields);
        new InjectAnnotationScanner(ownerClazz, Inject.class).addTo(mockDependentFields);
        Set<Object> mocks = new HashSet<>();
        mocks.addAll(extendMocks);
        mocks.addAll(this.mocks);
        inject(mockDependentFields, mocks);
    }

    private void inject(Set<Field> mockDependentFields, Set<Object> mocks){
        if(mockDependentFields.size() == 0 || mocks.size() == 0){
            return;
        }
        for(Field field : mockDependentFields){
            try {
                field.setAccessible(true);
                Object value = field.get(owner);
                mocks.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks, owner);
    }

    @Deprecated
    public void reset(){
        for(Object o : mocks){
            Mockito.reset(o);
        }
    }

    private void scanAndCreateMockitoFields(){
        if(mocks.size() > 0){
            return;
        }
        processMockitoAnnotations(ownerClazz, owner);
        new MockScanner(owner, ownerClazz).addPreparedMocks(mocks);
    }

    private void throwExceptionIfHasAnnotationMock(){
        for(Object o : mocks){
            if(!mockUtil.isSpy(o)){
                throw new RuntimeException("not support Mock annotation!");
            }
        }
    }

    /**
     * 清理单元测 录制
     * 清理集成测 录制
     */
    public void resetRecord(){
        Set<Field> mockDependentFields = new HashSet<Field>();
        new InjectAnnotationScanner(ownerClazz, Inject.class).addTo(mockDependentFields);
        resetMocks(mockDependentFields);
    }

    /**
     * 清理集成测 回放
     */
    public void resetTest(){
        Set<Field> mockDependentFields = new HashSet<Field>();
        new InjectAnnotationScanner(ownerClazz, Inject.class).addTo(mockDependentFields);
        new InjectAnnotationScanner(ownerClazz, InjectOnlyTest.class).addTo(mockDependentFields);
        resetMocks(mockDependentFields);
    }

    private void resetMocks(Set<Field> mockDependentFields){
        if(mockDependentFields.size() == 0){
            return;
        }
        Set<Object> oriTargetSet = getOriTargetSet();
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, oriTargetSet, owner);
    }

    private Set<Object> getOriTargetSet(){
        Set<Object> oriTargetSet = new HashSet<>();
        Set<Field> mockFields = new HashSet<Field>();
        new InjectAnnotationScanner(ownerClazz, Spy.class).addTo(mockFields);
        new InjectAnnotationScanner(ownerClazz, Mock.class).addTo(mockFields);
        if(mockFields.size() == 0){
            return new HashSet<>();
        }
        if(!isSpringContainer()){
            return new HashSet<>();
        }
        for(Field field : mockFields){
            Object oriTarget = getBean(field);
            if(oriTarget == null){
                continue;
            }
            oriTargetSet.add(oriTarget);
        }
        return oriTargetSet;
    }

    private Object getBean(Field field){
        Class fieldType = field.getType();
        Map<String, Object> beansMap = ColaMockito.g().getBeanFactory().getBeansOfType(fieldType);
        if(beansMap == null || beansMap.size() == 0){
            return null;
        }
        if(beansMap.size() == 1){
            return beansMap.values().toArray()[0];
        }
        String beanName = field.getName();
        Qualifier annotation = field.getAnnotation(Qualifier.class);
        if(annotation != null){
            beanName = annotation.value();
        }
        Iterator<Entry<String, Object>> iterator = beansMap.entrySet().iterator();
        while (iterator.hasNext()){
            Entry<String, Object> entry = iterator.next();
            if(beanName.equals(entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }

    private void processMockitoAnnotations(final Class<?> clazz, final Object testInstance) {
        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            //this will create @Mocks, @Captors, etc:
            delegate.process(classContext, testInstance);
            //this will create @Spies:
            spyAnnotationEngine.process(classContext, testInstance);
            processInjectAnnotation(classContext);
            classContext = classContext.getSuperclass();
        }
    }

    private boolean isSpringContainer(){
        if(ColaMockito.g().getBeanFactory() != null){
            return true;
        }
        return false;
    }

    private void processInjectAnnotation(Class clazz){
        Set<Field> mockDependentFields = new HashSet<Field>();
        new InjectAnnotationScanner(clazz, Inject.class).addTo(mockDependentFields);
        new InjectAnnotationScanner(clazz, InjectOnlyTest.class).addTo(mockDependentFields);

        for(Field f : mockDependentFields){
            FieldReader fr = new FieldReader(owner, f);
            Object value = null;
            if(!fr.isNull()){
                value = fr.read();
            }
            boolean isMockOrSpy = isMockOrSpy(value);
            if(value != null && isMockOrSpy){
                Mockito.reset(value);
                continue;
            }
            if(value == null){
                value = Mockito.spy(f.getType());
            }else{
                value = Mockito.spy(value);
            }
            new FieldSetter(owner, f).set(value);
        }
    }

    private boolean isMockOrSpy(Object instance) {
        if(instance == null){
            return false;
        }
        return mockUtil.isMock(instance)
            || mockUtil.isSpy(instance);
    }
}
