package com.alibaba.cola.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.exception.ColaException;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Hub holds all the important information about Command
 * 
 * @author fulan.zjf 2017-10-24
 */
@SuppressWarnings("rawtypes")
@Component
public class CommandHub{

    @Getter
    @Setter
    private ListMultimap<Class/*CommandClz*/, CommandInterceptorI> preInterceptors = LinkedListMultimap.create();
    @Getter
    @Setter
    private ListMultimap<Class/*CommandClz*/, CommandInterceptorI> postInterceptors = LinkedListMultimap.create();
    @Getter
    @Setter
    private List<CommandInterceptorI> globalPreInterceptors = new ArrayList<>(); //全局通用的PreInterceptors
    @Getter
    @Setter
    private List<CommandInterceptorI> globalPostInterceptors = new ArrayList<>(); //全局通用的PostInterceptors
    @Getter
    @Setter
    private Map<Class/*CommandClz*/, CommandInvocation> commandRepository = new HashMap<>();
    
    @Getter
    /**
     * This Repository is used for return right response type on exception scenarios
     */
    private Map<Class/*CommandClz*/, Class/*ResponseClz*/> responseRepository = new HashMap<>();
    
    public CommandInvocation getCommandInvocation(Class cmdClass) {
        CommandInvocation commandInvocation = commandRepository.get(cmdClass);
        if (commandInvocation == null) {
            //extends command, last parent Command
            Class<?> superClass = cmdClass.getSuperclass();
            if (!superClass.equals(Command.class)) {
                return getCommandInvocation(superClass);
            }
        }
        if (commandInvocation == null) {
            throw new ColaException(cmdClass + " is not registered in CommandHub, please register first");
        }
        return commandInvocation;
    }
}
