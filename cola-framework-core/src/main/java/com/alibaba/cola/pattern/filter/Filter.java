package com.alibaba.cola.pattern.filter;

/**
 *
 */
public interface Filter<T> {

	void doFilter(T context, FilterInvoker nextFilter);

}