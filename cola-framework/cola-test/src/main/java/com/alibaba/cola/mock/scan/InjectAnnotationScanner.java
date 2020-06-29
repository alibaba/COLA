package com.alibaba.cola.mock.scan;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.cola.mock.utils.Constants;

/**
 * @author shawnzhan.zxy
 * @since 2019/01/10
 */
public class InjectAnnotationScanner {
    private final Class<?> ownerClazz;
    private final Class annotation;

    public InjectAnnotationScanner(Class<?> ownerClazz, Class annotation) {
        this.ownerClazz = ownerClazz;
        this.annotation = annotation;
    }


    public void addTo(Set<Field> mockDependentFields) {
        mockDependentFields.addAll(scan());
    }

    private Set<Field> scan() {
        Set<Field> mockDependentFields = new HashSet<Field>();
        Field[] fields = ownerClazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(annotation)) {
                mockDependentFields.add(field);
            }
        }

        return mockDependentFields;
    }
}
