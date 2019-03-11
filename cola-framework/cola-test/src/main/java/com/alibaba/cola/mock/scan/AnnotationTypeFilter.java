package com.alibaba.cola.mock.scan;

import java.lang.annotation.Annotation;

/**
 * @author shawnzhan.zxy
 * @date 2018/10/07
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
        Annotation annotation = clazz.getDeclaredAnnotation(annotationCls);
        return annotation != null;
    }

    @Override
    public String getUid() {
        return uid;
    }
}
