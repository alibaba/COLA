package com.alibaba.cola.extension;

import com.alibaba.cola.context.Context;
import com.alibaba.cola.logger.Logger;
import com.alibaba.cola.logger.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author fulan.zjf
 * @date 2017/12/21
 */
public abstract class AbstractExecutorFacade {

    /**
     * Execute extension with Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <R> Response Type
     * @param <T> Parameter Type
     * @return
     */
    public <R, T> R execute(Class<T> targetClz, Context context, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, context);
        return exeFunction.apply(component);
    }

    /**
     * Execute extension without Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <T> Parameter Type
     */
    public <T> void executeVoid(Class<T> targetClz, Context context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }


    protected abstract <C> C locateComponent(Class<C> targetClz, Context context);
}
