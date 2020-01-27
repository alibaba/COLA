package com.alibaba.cola.domain;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
public interface DomainObjectFactoryI<T> {

    T create(Class<T> clazz,Object ... initargs);
}
