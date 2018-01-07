package com.alibaba.sofa.logger;

public class SysLogger implements Logger{

    public static SysLogger singleton = new SysLogger();

    @Override
    public void debug(String msg) {
        System.out.println("DEBUG: "+msg);
    }

    @Override
    public void info(String msg) {
        System.out.println("INFO: "+msg);
    }

    @Override
    public void warn(String msg) {
        System.out.println("WARN: "+msg);
    }

    @Override
    public void error(String msg) {
        System.err.println("ERROR: "+msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        System.err.println("ERROR: "+msg);
        t.printStackTrace();
    }

}
