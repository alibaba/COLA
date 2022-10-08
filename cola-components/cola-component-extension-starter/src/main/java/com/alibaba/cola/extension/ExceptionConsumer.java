package com.alibaba.cola.extension;

/**
 * @author huyuanxin
 */
@FunctionalInterface
public interface ExceptionConsumer<T> {

    void accept(T t) throws Exception;

}
