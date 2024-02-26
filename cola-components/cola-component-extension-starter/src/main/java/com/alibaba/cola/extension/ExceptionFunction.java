package com.alibaba.cola.extension;

import java.util.Objects;

/**
 * @author huyuanxin
 */
@FunctionalInterface
public interface ExceptionFunction<T, R> {

    R apply(T t) throws Exception;

    default <V> ExceptionFunction<V, R> compose(ExceptionFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     * @see #compose(ExceptionFunction)
     */
    default <V> ExceptionFunction<T, V> andThen(ExceptionFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> ExceptionFunction<T, T> identity() {
        return t -> t;
    }

}
