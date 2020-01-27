package com.alibaba.cola.boot;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.common.ColaConstant;
import com.alibaba.cola.exception.framework.ColaException;
import com.alibaba.cola.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2020/1/27
 * @description
 */
@Component
@AllArgsConstructor
public class RepositoryRegister implements RegisterI {

    private RepositoryHub repositoryHub;

    @Override
    public void doRegistration(Class<?> targetClz) {

       ClassParameterCheck classParameterCheck = new ClassParameterCheck(targetClz);
       Class<?>[] interfaces = targetClz.getInterfaces();
       for(Class<?> aInterface:interfaces){

           if(aInterface.isAssignableFrom(RepositoryCommandHandler.class)){
               Class<? extends CommandI> commandPresentation = classParameterCheck.getCommandPresentationFromExecutor();
               RepositoryCommandHandler commandHandler = (RepositoryCommandHandler) ApplicationContextHelper.getBean(targetClz);
               repositoryHub.getPresentationCommandRepository().put(commandPresentation, commandHandler);
           }

           if(aInterface.isAssignableFrom(RepositoryCommandResponseHandler.class)){
               Class<? extends CommandI> commandResponsePresentation = classParameterCheck.getCommandResponsePresentationFromExecutor();
               RepositoryCommandResponseHandler commandResponseHandler = (RepositoryCommandResponseHandler) ApplicationContextHelper.getBean(targetClz);
               repositoryHub.getPresentationCommandResponseRepository().put(commandResponsePresentation, commandResponseHandler);
           }

           if(aInterface.isAssignableFrom(RepositoryQueryHandler.class)){
               Class<? extends CommandI> queryPresentation = classParameterCheck.getQueryPresentationFromExecutor();
               RepositoryQueryHandler queryHandler = (RepositoryQueryHandler) ApplicationContextHelper.getBean(targetClz);
               repositoryHub.getPresentationQueryRepository().put(queryPresentation, queryHandler);
           }
        }

    }

    @Override
    public Class annotationType() {
        return RepositoryHandler.class;
    }

    private class ClassParameterCheck{
        Class<?> targetClz;
        Method[] methods;

        public ClassParameterCheck(Class<?> targetClz) {
            this.targetClz = targetClz;
            methods = targetClz.getDeclaredMethods();
        }


        private boolean hasParameter(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            if (exeParams.length == 0){
                throw new ColaException("Execute method in "+method.getDeclaringClass()+" should at least have one parameter");
            }
            return true;
        }

        private boolean noParameter(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            if (exeParams.length >0 ){
                throw new ColaException("Execute method in "+method.getDeclaringClass()+" should at have parameters");
            }
            return true;
        }

        private boolean parameter0IsPresentationI(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            if(!CommandI.class.isAssignableFrom(exeParams[0]) ){
                throw new ColaException("Execute method in "+method.getDeclaringClass()+" should be the subClass of PresentationI");
            }
            return true;
        }

        private boolean returnTypeIsResponse(Method method){
            if(!CmdResponseI.class.isAssignableFrom(method.getReturnType()) ){
                throw new ColaException("Execute method in "+method.getReturnType()+" should be the return type of Response");
            }
            return true;
        }

        private Class getPresentationI(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            return exeParams[0];
        }

        Class<? extends CommandI> getCommandResponsePresentationFromExecutor(){
            for (Method method : methods) {
                if (isCommandMethod(method)&&hasParameter(method) && parameter0IsPresentationI(method) && returnTypeIsResponse(method)){
                    return getPresentationI(method);
                }
            }
            throw new ColaException("Event param in " + targetClz + " command() is not detected");
        }

        Class<? extends CommandI> getQueryPresentationFromExecutor(){
            for (Method method : methods) {
                if (isQueryMethod(method)&&hasParameter(method) && parameter0IsPresentationI(method) && returnTypeIsResponse(method)){
                    return getPresentationI(method);
                }
            }
            throw new ColaException("Event param in " + targetClz + " command() is not detected");
        }

        Class<? extends CommandI> getCommandPresentationFromExecutor(){
            for (Method method : methods) {
                if (isCommandMethod(method)&&hasParameter(method) && parameter0IsPresentationI(method)){
                    return getPresentationI(method);
                }
            }
            throw new ColaException("Event param in " + targetClz + " command() is not detected");
        }


        private boolean isCommandMethod(Method method){
            return ColaConstant.COMMAND_METHOD.equals(method.getName()) && !method.isBridge();
        }

        private boolean isQueryMethod(Method method){
            return ColaConstant.QUERY_METHOD.equals(method.getName()) && !method.isBridge();
        }


    }

}
