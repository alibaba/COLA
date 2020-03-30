package com.alibaba.cola.mock.scan;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shawnzhan.zxy
 * @date 2019/01/10
 */
public class InjectAnnotationScanner {
    private final Class<?> clazz;
    private final Class annotation;

    public InjectAnnotationScanner(Class<?> clazz, Class annotation) {
        this.clazz = clazz;
        this.annotation = annotation;
    }


    public void addTo(Set<Field> mockDependentFields) {
        mockDependentFields.addAll(scan());
    }

    private Set<Field> scan() {
        Set<Field> mockDependentFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(annotation)) {
                mockDependentFields.add(field);
            }
        }

        return mockDependentFields;
    }
}
