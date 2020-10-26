package com.alibaba.cola.pattern.filter;

/**
 * 拦截器链
 * @author shawnzhan.zxy
 * @date 2018/04/17
 */
public class FilterChain<T>{

    private FilterInvoker header;

    public void doFilter(T context){
        header.invoke(context);
    }

    public void setHeader(FilterInvoker header) {
        this.header = header;
    }
}
