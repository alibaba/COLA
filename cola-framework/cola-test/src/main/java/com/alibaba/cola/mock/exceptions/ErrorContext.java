package com.alibaba.cola.mock.exceptions;

/**
 * @author shawnzhan.zxy
 * @since 2018/12/15
 */
public class ErrorContext {

    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<ErrorContext>();

    private String message;
    private Throwable cause;

    private ErrorContext() {
    }

    public static ErrorContext instance() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    public ErrorContext message(String message) {
        this.message = message;
        return this;
    }

    public ErrorContext cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public static boolean isError(){
        return LOCAL.get() != null;
    }

    public static void reset() {
        LOCAL.remove();
    }
}
