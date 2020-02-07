package com.alibaba.cola.boot;

import com.alibaba.cola.common.ApplicationContextHelper;
import com.alibaba.cola.repository.CommandI;
import com.alibaba.cola.repository.RepositoryHandler;
import com.alibaba.cola.repository.RepositoryHandlerI;
import com.alibaba.cola.repository.RepositoryHub;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

           if(aInterface.isAssignableFrom(RepositoryHandlerI.class)){
               List<Class<? extends CommandI>> commandPresentations = classParameterCheck.getCommandPresentationFromExecutor();
               for(Class<? extends CommandI> commandPresentation:commandPresentations) {
                   RepositoryHandlerI repositoryHandler = (RepositoryHandlerI) ApplicationContextHelper.getBean(targetClz);
                   repositoryHub.getPresentationRepository().put(commandPresentation, repositoryHandler);
               }
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

        private boolean haveOneParameter(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            if (exeParams.length == 1 ){
                return true;
            }
            return false;
        }

        private boolean parameter0IsPresentationI(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            if(CommandI.class.isAssignableFrom(exeParams[0]) ){
                return true;
            }
            return false;
        }


        private Class getPresentationI(Method method){
            Class<?>[] exeParams = method.getParameterTypes();
            return exeParams[0];
        }


        List<Class<? extends CommandI> > getCommandPresentationFromExecutor(){
            List<Class<? extends CommandI>> list = new ArrayList<>();
            for (Method method : methods) {
                if (haveOneParameter(method) && parameter0IsPresentationI(method)){
                    list.add(getPresentationI(method));
                }
            }
            return list;
        }
    }

}
