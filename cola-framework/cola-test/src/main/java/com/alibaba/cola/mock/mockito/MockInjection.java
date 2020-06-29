package com.alibaba.cola.mock.mockito;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mockito.internal.configuration.injection.ConstructorInjection;
import org.mockito.internal.configuration.injection.MockInjectionStrategy;
import org.mockito.internal.configuration.injection.SpyOnInjectedFieldsHandler;

import static org.mockito.internal.util.Checks.checkItemsNotNull;
import static org.mockito.internal.util.Checks.checkNotNull;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * @author shawnzhan.zxy
 * @since 2019/06/10
 */
public class MockInjection {

    /**
     * Create a new configuration setup for a field
     *
     *
     * @param field Field needing mock injection
     * @param ofInstance Instance owning the <code>field</code>
     * @return New configuration builder
     */
    public static OngoingMockInjection onField(Field field, Object ofInstance) {
        return new OngoingMockInjection(field, ofInstance);
    }

    /**
     * Create a new configuration setup for fields
     *
     *
     * @param fields Fields needing mock injection
     * @param ofInstance Instance owning the <code>field</code>
     * @return New configuration builder
     */
    public static OngoingMockInjection onFields(Set<Field> fields, Object ofInstance) {
        return new OngoingMockInjection(fields, ofInstance);
    }

    /**
     * Ongoing configuration of the mock injector.
     */
    public static class OngoingMockInjection {
        private final Set<Field> fields = new HashSet<Field>();
        private final Set<Object> mocks = newMockSafeHashSet();
        private final Object fieldOwner;
        private final MockInjectionStrategy injectionStrategies = MockInjectionStrategy.nop();
        private final MockInjectionStrategy postInjectionStrategies = MockInjectionStrategy.nop();

        private OngoingMockInjection(Field field, Object fieldOwner) {
            this(Collections.singleton(field), fieldOwner);
        }

        private OngoingMockInjection(Set<Field> fields, Object fieldOwner) {
            this.fieldOwner = checkNotNull(fieldOwner, "fieldOwner");
            this.fields.addAll(checkItemsNotNull(fields, "fields"));
        }

        public OngoingMockInjection withMocks(Set<Object> mocks) {
            this.mocks.addAll(checkNotNull(mocks, "mocks"));
            return this;
        }

        public OngoingMockInjection tryConstructorInjection() {
            injectionStrategies.thenTry(new ConstructorInjection());
            return this;
        }

        public OngoingMockInjection tryPropertyOrFieldInjection() {
            injectionStrategies.thenTry(new PropertyAndSetterInjection());
            return this;
        }

        public OngoingMockInjection handleSpyAnnotation() {
            postInjectionStrategies.thenTry(new SpyOnInjectedFieldsHandler());
            return this;
        }

        public void apply() {
            for (Field field : fields) {
                injectionStrategies.process(field, fieldOwner, mocks);
                postInjectionStrategies.process(field, fieldOwner, mocks);
            }
        }
    }

    public static void injectMocksOnFields(Set<Field> needingInjection, Set<Object> mocks, Object testClassInstance) {
        MockInjection.onFields(needingInjection, testClassInstance)
            .withMocks(mocks)
            .tryConstructorInjection()
            .tryPropertyOrFieldInjection()
            .handleSpyAnnotation()
            .apply();
    }
}
