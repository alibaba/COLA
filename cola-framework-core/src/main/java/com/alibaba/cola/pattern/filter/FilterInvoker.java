package com.alibaba.cola.pattern.filter;

/**
 * @author shawnzhan.zxy
 * @date 2018/04/17
 */
public interface FilterInvoker<T> {

    default public void invoke(T context){};
}
