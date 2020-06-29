package com.alibaba.cola.mock.mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.cola.mock.proxy.ColaProxyI;
import com.alibaba.cola.mock.proxy.MockDataProxy;
import com.alibaba.cola.mock.utils.Constants;
import com.alibaba.cola.mock.utils.reflection.BeanPropertySetter;

import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;

import static com.alibaba.cola.mock.utils.Constants.COLAMOCK_PROXY_FLAG;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * @author shawnzhan.zxy
 * @since 2019/06/10
 */
public class PropertyAndSetterInjection extends MockInjectionStrategy {
    private static final String CGLIB_CALLBACK = "CGLIB$CALLBACK_0";

    private final MockCandidateFilter
        mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private final Comparator<Field>
        superTypesLast = new FieldTypeAndNameComparator();

    private final ListUtil.Filter<Field> notFinalOrStatic = new ListUtil.Filter<Field>() {
        @Override
        public boolean isOut(Field object) {
            return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
        }
    };

    @Override
    public boolean processInjection(Field injectMocksField, Object injectMocksFieldOwner, Set<Object> mockCandidates) {
        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = injectMocksField.getType();
        Object fieldInstanceNeedingInjection = getFieldValue(injectMocksField, injectMocksFieldOwner);
        while (fieldClass != Object.class) {
            injectionOccurred |= injectMockCandidates(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection);
            fieldClass = fieldClass.getSuperclass();
        }
        return injectionOccurred;
    }

    private Object getFieldValue(Field injectMocksField, Object injectMocksFieldOwner){
        Object value = null;
        try {
            injectMocksField.setAccessible(true);
            value = injectMocksField.get(injectMocksFieldOwner);
            //获取到真实的target，而不是代理对象
            while(value != null && value.getClass().getName().indexOf(COLAMOCK_PROXY_FLAG) > 0){
                BeanPropertySetter beanProperty = new BeanPropertySetter(value, CGLIB_CALLBACK);
                value = ((ColaProxyI)beanProperty.getValue()).getInstance();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    private boolean injectMockCandidates(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object instance) {
        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
        // pass 1
        injectionOccurred |= injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        // pass 2
        injectionOccurred |= injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
        return injectionOccurred;
    }

    private boolean injectMockCandidatesOnFields(Set<Object> mocks, Object instance, boolean injectionOccurred, List<Field> orderedInstanceFields) {
        for (Iterator<Field> it = orderedInstanceFields.iterator(); it.hasNext(); ) {
            Field field = it.next();
            Object injected = mockCandidateFilter.filterCandidate(mocks, field, instance).thenInject();
            if (injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
                it.remove();
            }
        }
        return injectionOccurred;
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        declaredFields = ListUtil.filter(declaredFields, notFinalOrStatic);

        Collections.sort(declaredFields, superTypesLast);

        return declaredFields;
    }

    static class FieldTypeAndNameComparator implements Comparator<Field> {
        @Override
        public int compare(Field field1, Field field2) {
            Class<?> field1Type = field1.getType();
            Class<?> field2Type = field2.getType();

            // if same type, compares on field name
            if (field1Type == field2Type) {
                return field1.getName().compareTo(field2.getName());
            }
            if(field1Type.isAssignableFrom(field2Type)) {
                return 1;
            }
            if(field2Type.isAssignableFrom(field1Type)) {
                return -1;
            }
            return 0;
        }
    }
}
