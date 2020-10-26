package com.alibaba.cola.logger;

public class SLFJLogger implements com.alibaba.cola.logger.Logger{

    private org.slf4j.Logger slfjLogger;
    
    public SLFJLogger(org.slf4j.Logger slfjLogger) {
        this.slfjLogger = slfjLogger;
    }
    
    @Override
    public void debug(String msg) {
        slfjLogger.debug(msg);
    }

    @Override
    public void info(String msg) {
        slfjLogger.info(msg);
        
    }

    @Override
    public void warn(String msg) {
        slfjLogger.warn(msg);
        
    }

    @Override
    public void error(String msg) {
        slfjLogger.error(msg);
        
    }

    @Override
    public void error(String msg, Throwable t) {
        slfjLogger.error(msg, t);
    }

}
