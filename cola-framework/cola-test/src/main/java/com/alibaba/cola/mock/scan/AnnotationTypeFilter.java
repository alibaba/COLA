package com.alibaba.cola.mock.scan;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author shawnzhan.zxy
 * @since 2018/10/07
 */
public class AnnotationTypeFilter implements TypeFilter{
    private String uid;
    private Class<? extends Annotation> annotationCls;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationCls){
        this.annotationCls = annotationCls;
        this.uid = annotationCls.toString();
    }

    @Override
    public boolean match(Class clazz) {
        Annotation annotation = AnnotationUtils.findAnnotation(clazz, annotationCls);
        return annotation != null;
    }

    @Override
    public String getUid() {
        return uid;
    }
}
