package com.alibaba.cola.logger;

public class SysLogger implements Logger{

    private String loggerName;

    private SysLogger(String loggerName){
        this.loggerName = loggerName;
    }

    public static Logger getLogger(Class clz){
        return new SysLogger(clz.getName());
    }


    public static Logger getLogger(String loggerName){
        return new SysLogger(loggerName);
    }

    @Override
    public void debug(String msg) {
        System.out.println("["+loggerName+"] DEBUG: "+msg);
    }

    @Override
    public void info(String msg) {
        System.out.println("["+loggerName+"] INFO: "+msg);
    }

    @Override
    public void warn(String msg) {
        System.out.println("["+loggerName+"] WARN: "+msg);
    }

    @Override
    public void error(String msg) {
        System.err.println("["+loggerName+"] ERROR: "+msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        System.err.println("["+loggerName+"] ERROR: "+msg);
        t.printStackTrace();
    }

}
