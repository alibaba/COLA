package com.alibaba.cola.repository;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author lorne
 * @date 2020/1/27
 */
@Component
@AllArgsConstructor
public class RepositoryBus {


    private RepositoryHub repositoryHub;


    public Object command(CommandI command){
        RepositoryHandlerI presentationHandler =  repositoryHub.getPresentationRepository(command.getClass());
        try {
            return MethodUtils.invokeExactMethod(presentationHandler,command.command(),command);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }




}
