package com.alibaba.cola.catchlog;

import com.alibaba.cola.exception.BaseException;
import com.alibaba.cola.exception.BizException;
import com.alibaba.cola.exception.SysException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @ Description   :  Catching and Logging
 * @ Author        :  Frank Zhang
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Aspect
@Slf4j
public class CatchLogAspect {

    /**
     * The syntax of pointcut : https://blog.csdn.net/zhengchao1991/article/details/53391244
     */
    @Pointcut("@within(CatchAndLog) && execution(public * *(..))")
    public void pointcut(){
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint ) {
        long startTime =  System.currentTimeMillis();

        logRequest(joinPoint);

        Object response = null;
        try {
             response = joinPoint.proceed();
        }
        catch (Throwable e){
            response = handleException(joinPoint, e);
        }
        finally {
            logResponse(startTime, response);
        }

        return response ;
    }

    private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) {
        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        Class returnType = ms.getReturnType();

        if (e instanceof BizException){
            log.warn("BIZ EXCEPTION : " + e.getMessage());
            //在Debug的时候，对于BizException也打印堆栈
            if(log.isDebugEnabled()){
                log.error(e.getMessage(), e);
            }
            return ResponseHandler.handle(returnType, (BaseException)e);
        }

        if (e instanceof SysException){
            log.error("SYS EXCEPTION :");
            log.error(e.getMessage(), e);
            return ResponseHandler.handle(returnType, (BaseException)e);
        }

        log.error("UNKNOWN EXCEPTION :");
        log.error(e.getMessage(), e);

        return ResponseHandler.handle(returnType, "UNKNOWN_ERROR", e.getMessage());
    }


    private void logResponse(long startTime, Object response) {
        try{
            long endTime =  System.currentTimeMillis();
            log.debug("RESPONSE : "+ JSON.toJSONString(response) );
            log.debug("COST : " + (endTime - startTime) + "ms");
        }
        catch (Exception e){
            //swallow it
            log.error("logResponse error : " + e);
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint) {
        try {
            log.debug("START PROCESSING: " + joinPoint.getSignature().toShortString());
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                log.debug("REQUEST : " + JSON.toJSONString(arg));
            }
        }
        catch (Exception e){
            //swallow it
            log.error("logReqeust error : " + e);
        }
    }

}
