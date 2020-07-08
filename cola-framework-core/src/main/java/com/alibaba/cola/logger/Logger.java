package com.alibaba.cola.logger;

/**
 * This is a typical DIP, by depending on ourselves logger, we can change the underline logger vendor easily
 *
 * @author fulan.zjf 2017年10月23日 下午11:58:54
 */
public interface Logger {

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg the message string to be logged
     */
    public void debug(String msg);

    /**
     * Log a message at the DEBUG level. support format.
     *
     * @param msg
     * @param args
     */
    default public void debug(String msg, Object... args){
    	debug(String.format(msg, args));
    }

    /**
     * Log a message at the INFO level.
     *
     * @param msg the message string to be logged
     */
    public void info(String msg);

    /**
     * Log a message at the INFO level. support format.
     *
     * @param msg
     * @param args
     */
    default public void info(String msg, Object... args){
    	info(String.format(msg, args));
    }

    /**
     * Log a message at the WARN level.
     *
     * @param msg the message string to be logged
     */
    public void warn(String msg);

    /**
     * Log a message at the WARN level. support format.
     *
     * @param msg
     * @param args
     */
    default public void warn(String msg, Object... args){
    	warn(String.format(msg, args));
    }
    /**
     * Log a message at the ERROR level.
     *
     * @param msg the message string to be logged
     */
    public void error(String msg);

    /**
     * Log a message at the ERROR level. support format.
     * @param msg
     * @param args
     */
    default public void error(String msg, Object... args){
    	error(String.format(msg, args));
    }

    /**
     * Log an exception (throwable) at the ERROR level with an
     * accompanying message.
     *
     * @param msg the message accompanying the exception
     * @param t   the exception (throwable) to log
     */
    public void error(String msg, Throwable t);
}
