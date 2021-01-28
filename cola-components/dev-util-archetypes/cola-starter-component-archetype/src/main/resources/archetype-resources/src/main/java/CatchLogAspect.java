#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

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
        //Dummy implementation
        return null;
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
