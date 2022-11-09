package com.alibaba.cola.extension.register;

import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionCoordinate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author fulan.zjf
 * @date 2017/12/21
 */
public abstract class AbstractComponentExecutor {

    /**
     * Execute extension with Response
     *
     * @param targetClz
     * @param bizScenario
     * @param exeFunction
     * @param <R> Response Type
     * @param <T> Parameter Type
     * @return
     */
    public <R, T> R execute(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, bizScenario);
        return exeFunction.apply(component);
    }

    public <R, T> R execute(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction){
        return execute((Class<T>) extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    public <R, T> CompletableFuture<R> executeAsync(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction) {
        return executeAsync(targetClz, bizScenario, exeFunction, null);
    }

    /**
     * the async execute api. if the param {executor} is null, will use forkJoin pool
     * other similar.
     */
    public <R, T> CompletableFuture<R> executeAsync(Class<T> targetClz, BizScenario bizScenario, Function<T, R> exeFunction, Executor executor) {
        return CompletableFuture.supplyAsync(() -> execute(targetClz, bizScenario, exeFunction), Objects.nonNull(executor) ? executor : ForkJoinPool.commonPool());
    }

    public <R, T> CompletableFuture<R> executeAsync(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction) {
        return executeAsync(extensionCoordinate, exeFunction, null);
    }

    public <R, T> CompletableFuture<R> executeAsync(ExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction, Executor executor) {
        return CompletableFuture.supplyAsync(() -> execute(extensionCoordinate, exeFunction), Objects.nonNull(executor) ? executor : ForkJoinPool.commonPool());
    }

    /**
     * Execute extension without Response
     *
     * @param targetClz
     * @param context
     * @param exeFunction
     * @param <T> Parameter Type
     */
    public <T> void executeVoid(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }

    public <T> void executeVoid(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction){
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    public <T> void executeVoidAsync(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction) {
        executeVoidAsync(targetClz, context, exeFunction, null);
    }

    public <T> void executeVoidAsync(Class<T> targetClz, BizScenario context, Consumer<T> exeFunction, Executor executor) {
        CompletableFuture.runAsync(() -> executeVoid(targetClz, context, exeFunction), Objects.nonNull(executor) ? executor : ForkJoinPool.commonPool());
    }

    public <T> void executeVoidAsync(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
        executeVoidAsync(extensionCoordinate, exeFunction, null);
    }

    public <T> void executeVoidAsync(ExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction, Executor executor) {
        CompletableFuture.runAsync(() -> executeVoid(extensionCoordinate, exeFunction), Objects.nonNull(executor) ? executor : ForkJoinPool.commonPool());
    }

    protected abstract <C> C locateComponent(Class<C> targetClz, BizScenario context);
}
